import {
  Widget,
  addUserMessage,
  addResponseMessage,
  renderCustomComponent,
  deleteMessages,
  setBadgeCount,
} from 'react-chat-widget';
import 'react-chat-widget/lib/styles.css';
import './styles.css';
import {useEffect, useRef, useState} from 'react';
import database from '../../config/firebase';
import moment from 'moment';
const Chat = ({senderId, receiverId, conversationKey, isCompany}) => {
  const messageRef = useRef(database.ref(`chats/${conversationKey}/messages`));

  const infoRef = useRef(null);
  const receiverUnreadRef = useRef(null);
  const [opponentAvatar, setOpponentAvatar] = useState('');
  const [opponentName, setOpponentName] = useState('');

  const handleSendMessage = (newMessage) => {
    messageRef.current.push({
      message: newMessage,
      senderId: senderId,
      timestamp: new Date().getTime(),
    });

    infoRef.current.child('unread').set(0);

    receiverUnreadRef.current.once('value', (snapshot) => {
      const data = snapshot.val();
      receiverUnreadRef.current.set(data + 1);
    });
    renderCustomComponent(SendTime, {
      timestamp: new Date().getTime(),
      isSender: true,
    });
  };

  const fetchMessages = () => {
    messageRef.current.once('value', (snapshot) => {
      snapshot.forEach((child) => {
        const data = child.val();
        console.log(data, receiverId);
        if (data['senderId'] === receiverId) {
          addResponseMessage(data['message']);
          renderCustomComponent(SendTime, {
            timestamp: data['timestamp'],
            isSender: false,
          });
        }
        if (data['senderId'] === senderId) {
          addUserMessage(data['message']);
          renderCustomComponent(SendTime, {
            timestamp: data['timestamp'],
            isSender: true,
          });
        }
      });
    });
  };

  useEffect(() => {
    // listen new messages
    messageRef.current
      .orderByChild('timestamp')
      .startAt(Date.now())
      .limitToLast(1)
      .on('child_added', (snapshot) => {
        const data = snapshot.val();
        if (data['senderId'] === receiverId) {
          addResponseMessage(data['message']);
          renderCustomComponent(SendTime, {
            timestamp: data['timestamp'],
            isSender: false,
          });
        }
      });

    // listen to badge

    if (isCompany) {
      infoRef.current = database
        .ref('companies_keys/')
        .child(senderId)
        .child(conversationKey);

      receiverUnreadRef.current = database
        .ref('users_keys/')
        .child(receiverId)
        .child(conversationKey);
    } else {
      infoRef.current = database
        .ref('users_keys/')
        .child(senderId)
        .child(conversationKey);
      receiverUnreadRef.current = database
        .ref('companies_keys/')
        .child(receiverId)
        .child(conversationKey)
        .child('unread');
    }

    infoRef.current.once('value', (snapshot) => {
      const data = snapshot.val();
      if (data) {
        setOpponentAvatar(data['avatar']);
        setOpponentName(data['name']);
      }
    });

    infoRef.current.child('unread').on('value', (snapshot) => {
      const data = snapshot.val();

      if (data !== 0) {
        setBadgeCount(data);
      }
    });

    // init
    fetchMessages();

    return () => {
      deleteMessages();
      infoRef.current.child('unread').off('value');
      messageRef.current.off('child_added');
    };
  }, []);

  return (
    <Widget
      showCloseButton={true}
      chatId={conversationKey}
      emojis={true}
      titleAvatar={opponentAvatar}
      title="Tư vấn trực tuyến"
      subtitle={opponentName}
      handleNewUserMessage={handleSendMessage}
      senderPlaceHolder={'Nhập tin nhắn của bạn'}
      showTimeStamp={false}
    />
  );
};

const SendTime = ({timestamp, isSender}) => {
  return (
    <div
      className={[
        'text-muted',
        'chat-time',
        isSender ? 'sender' : 'receiver',
      ].join(' ')}
    >
      {moment(timestamp).fromNow()}
    </div>
  );
};

export default Chat;
