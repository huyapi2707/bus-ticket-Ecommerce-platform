package org.huydd.bus_ticket_Ecommercial_platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface FilterAndPaginateRepository<T, U> extends JpaRepository<T, U>, JpaSpecificationExecutor<T> {

}
