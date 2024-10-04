import {useContext, useEffect, useRef, useState} from 'react';
import './styles.css';
import {AuthenticationContext, CompanyContext} from '../../config/context';
import database from '../../config/firebase';
import moment from 'moment';
const ManagerChatSupport = () => {
  const {company} = useContext(CompanyContext);
  const {user} = useContext(AuthenticationContext);
  const conversationListRef = useRef(null);
  const conversationRef = useRef(null);
  const opponentRef = useRef(null);
  const [conversations, setConversations] = useState({});
  const [selectedConversationKey, setSelectedConversationKey] = useState(null);
  const [messages, setMessages] = useState({});
  const [newMessage, setNewMessage] = useState('');
  const fetchConversationList = async () => {
    let tempConversation = {};
    await conversationListRef.current.once('value').then((snapshot) => {
      tempConversation = snapshot.val();
    });
    Object.keys(tempConversation).map((key) =>
      conversationRef.current
        .child('/' + key + '/messages')
        .limitToLast(1)
        .once('value')
        .then((messageSnapshot) => {
          let lastestMessage = '';
          messageSnapshot.forEach((messageRef) => {
            lastestMessage = messageRef.val().message;
            tempConversation[key]['lastestMessage'] = lastestMessage;
          });
        })
        .then(() => {
          setConversations(tempConversation);
        }),
    );
  };

  const handleChangeConversation = (key) => {
    opponentRef.current = database
      .ref('users_keys')
      .child('/' + conversations[key]['opponentId'])
      .child(key);
    conversationListRef.current.child(key).child('unread').set(0);
    setSelectedConversationKey(key);
  };
  const handleSendMessage = () => {
    conversationRef.current
      .child('/' + selectedConversationKey + '/messages')
      .push({
        message: newMessage,
        senderId: company['id'],
        timestamp: new Date().getTime(),
      });
    opponentRef.current.child('unread').once('value', (snapshot) => {
      const data = snapshot.val();
      opponentRef.current.child('unread').set(data + 1);
    });
    setNewMessage('');
  };
  useEffect(() => {
    // init
    conversationListRef.current = database
      .ref('companies_keys')
      .child('/' + company['id']);
    conversationRef.current = database.ref('chats');
    fetchConversationList();
    // listen to new conversation
    conversationListRef.current.on('child_changed', (snapshot) => {
      setConversations((conversations) => {
        return {
          ...conversations,
          [snapshot.key]: snapshot.val(),
        };
      });
    });

    return () => {
      conversationListRef.current.off('child_changed');
    };
  }, []);
  useEffect(() => {
    if (selectedConversationKey != null) {
      // read last 5 messages
      let tempMessages = {};
      conversationRef.current
        .child('/' + selectedConversationKey + '/messages')
        .orderByChild('timestamp')
        .limitToLast(5)
        .once('value')
        .then((snapshot) => {
          snapshot.forEach((message) => {
            tempMessages = {
              ...tempMessages,
              [message.key]: message.val(),
            };
          });
        })
        .then(() => {
          setMessages(tempMessages);
        });
      // listen to new message
      conversationRef.current
        .child('/' + selectedConversationKey + '/messages')
        .orderByChild('timestamp')
        .startAt(Date.now())
        .on('child_added', (snapshot) => {
          setMessages((messages) => {
            return {
              ...messages,
              [snapshot.key]: snapshot.val(),
            };
          });
        });
    }
    return () => {
      conversationRef.current
        .child('/' + selectedConversationKey + '/messages')
        .off('child_added');
    };
  }, [selectedConversationKey]);
  const isSender = (senderId) => {
    return senderId === company['id'];
  };
  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col-md-4 chat-section overflow-auto">
          {conversations &&
            Object.keys(conversations).map((key, index) => {
              return (
                <div
                  onClick={() => handleChangeConversation(key)}
                  className="d-flex border-bottom p-3 m-3 conversation"
                  key={index}
                >
                  <div className="c-avatar d-flex align-items-center">
                    <img
                      src={conversations[key]['avatar']}
                      className="rounded-circle img-fluid "
                      alt="avatar"
                    ></img>
                  </div>
                  <div className="c-content">
                    <div>
                      <p className="fs-6 fw-bold m-0 text-truncate">
                        {conversations[key]['name']}
                      </p>
                    </div>
                    <div>
                      <p className="m-0 text-truncate">
                        {conversations[key]['lastestMessage']}
                      </p>
                    </div>
                  </div>
                  <div className="c-badge">
                    <p>
                      <span className="badge bg-secondary  bg-danger">
                        {conversations[key]['unread'] || ''}
                      </span>
                    </p>
                  </div>
                </div>
              );
            })}
        </div>
        {selectedConversationKey && (
          <div className="col-md-8 chat-section border-start">
            <div className="d-flex chat-section-header border-bottom p-3 m-3 shadow">
              <div className="h-100 d-flex align-items-center">
                <img
                  alt="avatar"
                  src={conversations[selectedConversationKey]['avatar']}
                  className="rounded-circle img-fluid"
                ></img>
              </div>
              <div className="d-flex align-items-center">
                <p className="fw-bold fs-4 p-0">
                  {conversations[selectedConversationKey]['name']}
                </p>
              </div>
            </div>
            <div className="chat-container p-3 m-3">
              {Object.keys(messages).map((key, index) => {
                return (
                  <div
                    className={[
                      'my-3 d-flex flex-column px-2',
                      isSender(messages[key]['senderId'])
                        ? 'senderMessage'
                        : 'opponentMessage',
                    ].join(' ')}
                    key={index}
                  >
                    <div className="message p-3">
                      <p className="m-0">{messages[key]['message']}</p>
                    </div>
                    <div className="timestamp">
                      <span className="text-muted">
                        {moment(messages[key]['timestamp']).fromNow()}
                      </span>
                    </div>
                  </div>
                );
              })}
            </div>
            <div className="chat-input-container m-3 d-flex align-items-center">
              <div className="me-2 input">
                <input
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                  className="form-control"
                  type="text"
                  multiple={true}
                ></input>
              </div>
              <div className="btn-send">
                <button
                  onClick={handleSendMessage}
                  className="btn btn-secondary"
                >
                  Gửi
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ManagerChatSupport;
