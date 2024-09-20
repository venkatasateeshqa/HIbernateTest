package com.neoteric.dbconnection.service;
import com.neoteric.dbconnection.entity.AccountAddressEntity;
import com.neoteric.dbconnection.entity.AccountEntity;
import com.neoteric.dbconnection.exception.AccountCreationFailedException;
import com.neoteric.dbconnection.hibernate.HibernateUtils;
import com.neoteric.dbconnection.model.Account;
import com.neoteric.dbconnection.model.Address;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AccountService {

    public Account searchAccountByJpa(String accountNumber){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("jpaDemo");
        EntityManager entityManager=emf.createEntityManager();
        entityManager.getTransaction().begin();
        jakarta.persistence.Query query= entityManager.createQuery("select a from AccountEntity a where a.accountNumber=:inputAccountNumber ");
        query.setParameter("inputAccountNumber",accountNumber);

        List<AccountEntity> accountEntities = query.getResultList();
        AccountEntity accountEntity=accountEntities.get(0);
        Account account= Account.builder()
                .accountNumber(accountEntity.getAccountNumber())
                .mobileNumber(accountEntity.getMobileNumber())
                .pan(accountEntity.getPan())
                .balance(accountEntity.getBalance())
                .name(accountEntity.getName())
                .build();
        List<AccountAddressEntity> accountAddressEntityList=
                accountEntity.getAccountAddressEntitiesList();

        if (Objects.nonNull(accountAddressEntityList)&& accountAddressEntityList.size()>0){

            AccountAddressEntity accountAddressEntity=accountAddressEntityList.get(0);
            System.out.println("AccountAddressEntity is Loaded");
            Address address=new Address();
            address.setAdd1(accountAddressEntity.getAddress1());
            address.setAdd2(accountAddressEntity.getAddress2());
            address.setPincode(accountAddressEntity.getPincode());
            address.setCity(accountAddressEntity.getCity());
            address.setState(accountAddressEntity.getState());
            account.setAddress(address);
        }
        entityManager.getTransaction().commit();
        return account;

    }

    public String createAccountByJpa(Account account) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaDemo");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        AccountEntity accountEntity = new AccountEntity();
        String randomAccountNumber = UUID.randomUUID().toString();
        accountEntity.setAccountNumber(randomAccountNumber);
        accountEntity.setMobileNumber(account.getMobileNumber());
        accountEntity.setPan(account.getPan());
        accountEntity.setBalance(account.getBalance());
        accountEntity.setName(account.getName());

        List<AccountAddressEntity> addressEntities=new ArrayList<>();
        AccountAddressEntity accountAddressEntity=new AccountAddressEntity();

        accountAddressEntity.setAddress1(account.getAddress().getAdd1());
        accountAddressEntity.setAddress2(account.getAddress().getAdd2());
        accountAddressEntity.setCity(account.getAddress().getCity());
        accountAddressEntity.setState(account.getAddress().getState());
        accountAddressEntity.setPincode(account.getAddress().getPincode());
        accountAddressEntity.setStatus(1);
        accountAddressEntity.setAccountEntity(accountEntity);

        addressEntities.add(accountAddressEntity);
        accountEntity.setAccountAddressEntitiesList(addressEntities);

        entityManager.persist(accountEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
        emf.close();
        account.setAccountNumber(randomAccountNumber);
        return randomAccountNumber;
    }



    public Account searchAccount(String accountNumber){
        SessionFactory sessionFactory=HibernateUtils.getSessionFactory();
        Session session=sessionFactory.openSession();
        Query<AccountEntity> query=session.createQuery("From AccountEntity a where a.accountNumber= :inputAccountNumber");
        query.setParameter("inputAccountNumber",accountNumber);
        AccountEntity accountEntity=query.list().get(0);

        AccountAddressEntity addressEntity = accountEntity.getAccountAddressEntitiesList().stream().findAny().orElse(null);
        Address address = null;
        if (addressEntity != null) {
            address = Address.builder()
                    .add1(addressEntity.getAddress1())
                    .add2(addressEntity.getAddress2())
                    .city(addressEntity.getCity())
                    .state(addressEntity.getState())
                    .pincode(addressEntity.getPincode())
                    .build();
        }

        Account account=Account.builder().accountNumber(accountEntity.getAccountNumber()).
                mobileNumber(accountEntity.getMobileNumber()).balance(accountEntity.getBalance())
                .pan(accountEntity.getPan()).address(address).build();
        return account;
    }

    public String oneToManyUsingUI(Account account){
        SessionFactory sessionFactory=HibernateUtils.getSessionFactory();
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        AccountEntity accountEntity=new AccountEntity();
        accountEntity.setAccountNumber(UUID.randomUUID().toString());
        accountEntity.setName(account.getName());
        accountEntity.setPan(account.getPan());
        accountEntity.setBalance(account.getBalance());
        accountEntity.setMobileNumber(account.getMobileNumber());

        List<AccountAddressEntity> accountAddressEntityList=new ArrayList<>();
        AccountAddressEntity accountAddressEntity=new AccountAddressEntity();
        accountAddressEntity.setAddress1(account.getAddress().getAdd1());
        accountAddressEntity.setAddress2(account.getAddress().getAdd2());
        accountAddressEntity.setCity(account.getAddress().getCity());
        accountAddressEntity.setState(account.getAddress().getState());
        accountAddressEntity.setPincode(account.getAddress().getPincode());
        accountAddressEntity.setStatus(1);
        accountAddressEntity.setAccountEntity(accountEntity);

        accountAddressEntityList.add(accountAddressEntity);
        accountEntity.setAccountAddressEntitiesList(accountAddressEntityList);
        session.persist(accountEntity);
        transaction.commit();
        return accountEntity.getAccountNumber();

    }

    public  String createAccountUsingHibernate(Account account){
        SessionFactory sessionFactory= HibernateUtils.getSessionFactory();
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        AccountEntity accountEntity=new AccountEntity();
        accountEntity.setAccountNumber(UUID.randomUUID().toString());
        accountEntity.setName(account.getName());
        accountEntity.setPan(account.getPan());
        accountEntity.setBalance(account.getBalance());
        accountEntity.setMobileNumber(account.getMobileNumber());

        session.persist(accountEntity);
        transaction.commit();
        return accountEntity.getAccountNumber();
    }

    public String createAccount(Account account) throws AccountCreationFailedException {
        String accountNumber=null;
        try {
            Connection connection=DBConnection.getConnection();
            Statement stmt=connection.createStatement();
            accountNumber= UUID.randomUUID().toString();

            String query="insert into bank.account value ("+"'"+accountNumber+"'"+","+
                    "'"+account.getName()+"'"+","+
                    "'"+account.getPan()+"'"+","+
                    "'"+account.getMobileNumber()+ "'"+","+
                    account.getBalance()+")";

            int status=stmt.executeUpdate(query);

            if(status==1){

                System.out.println("Account is created"+accountNumber);
            }else{
                throw new AccountCreationFailedException("Account creation is failed"+account.getPan());
            }


        }catch (SQLException e){
            System.out.println("Exception occured in sql exception"+e);
        }
        catch (AccountCreationFailedException e){
            System.out.println("Exception occured in service"+e);
            throw e;
        }
        return accountNumber;
    }
}
