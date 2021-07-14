package io.muic.ooc.webapp.service;

import io.muic.ooc.webapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user(username, password, display_name) VALUES (?,?,?);";
    private static final String SELECT_USER_SQL = "SELECT * FROM tbl_user WHERE username = ?;";
    private static final String SELECT_ALL_USERs_SQL = "SELECT * FROM tbl_user ;";
    private static final String DELETE_USER_SQL = "DELETE FROM tbl_user WHERE username = ?;";



    private DatabaseConnectionService databaseConnectionService;

    private static UserService service;

    public UserService() {
    }

    public static UserService getInstance() {
       if(service==null){
           service=new UserService();
           service.setDatabaseConnectionService(DatabaseConnectionService.getInstance());
       }
       return service;
    }

    public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    public void createUser(String username, String password, String displayName) throws UserServiceException {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);
                ){

            ps.setString(1, username);

            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            ps.setString(3, displayName);
            ps.executeUpdate();

            connection.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UsernameNotUniqueException(String.format(" Username %s has already been taken.", username));
        } catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }
    }

    public User findByUsername(String username) {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_USER_SQL);
                ){

            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("display_name"));
        } catch (SQLException throwables) {
            return null;
        }
    }

    public List<User> findALl() {
        List<User> users = new ArrayList<>();

        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL_USERs_SQL);
                ){


            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
                users.add(
                        new User(
                                resultSet.getLong("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("display_name")));
        } catch (SQLException throwables) {
            return null;
        }
        return users;
    }

    public boolean deleteUserByUsername(String username){
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL);
        ){

            ps.setString(1, username);
            int deleteCount= ps.executeUpdate();
            connection.commit();
            return deleteCount > 0;

        } catch (SQLException throwables) {
            return false;
        }
    }

    public boolean deleteUserById(Long id){
        throw new UnsupportedOperationException("not yet implemented");
    }
}
