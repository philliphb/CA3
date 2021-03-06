/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import entity.ExchangeRates;
import entity.User;
import java.util.List;

/**
 *
 * @author lam
 */
public interface IUserFacade {

    /*
    Return the Roles if users could be authenticated, otherwise null
     */
    List<String> authenticateUser(String userName, String password);

    IUser getUserByUserId(String id);
    
    void addUser(String username, String password);
    
    List<User> getAllUsers();

    public Object deleteUser(String id);
    
    void addExchangeRates(ExchangeRates exRate);
    
    ExchangeRates getExhangeRates(String id);
    
    String getRateByCode(String code);
    
}
