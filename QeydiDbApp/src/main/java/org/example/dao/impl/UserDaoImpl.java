package org.example.dao.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.dao.inter.AbstractDao;
import org.example.dao.inter.UserDaoInter;
import org.example.entity.Subject;
import org.example.entity.Teachway;
import org.example.entity.User;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends AbstractDao implements UserDaoInter {
    public User getAllUser(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String university = rs.getString("university");
        String experience = rs.getString("experience");
        String email = rs.getString("email");
        String code = rs.getString("code");
        String password = rs.getString("password");
        int age = rs.getInt("age");
        int point = rs.getInt("point");
        double cost = rs.getDouble("cost");

        int teachwayId = rs.getInt("teach_id");
        int subjectId = rs.getInt("subject_id");
        String teachwayStr = rs.getString("teachway");
        String subjectStr = rs.getString("subject");

        Teachway teachway = new Teachway(teachwayId, teachwayStr);
        Subject subject = new Subject(subjectId, subjectStr);

        User u = new User(id, name, surname, age, university, point, experience, teachway, subject, email, code, cost, password);
        return u;
    }

    public User getSimpleAll(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String university = rs.getString("university");
        String experience = rs.getString("experience");
        String email = rs.getString("email");
        String code = rs.getString("code");
        String password = rs.getString("password");
        int age = rs.getInt("age");
        int point = rs.getInt("point");
        double cost = rs.getDouble("cost");

        int teachwayId = rs.getInt("teach_id");
        int subjectId = rs.getInt("subject_id");
        User u = new User(id, name, surname, age, university, point, experience, null, null, email, code, cost, password);

        return u;
    }

    @Override
    public List<User> getAll(String name, String surname, String email, String password) {
        List<User> result = new ArrayList<>();

        try (Connection c = connect()) {
            String sql = "SELECT u.*,t.teachway,s.subject " +
                    " FROM user u " +
                    " left join teach_table t " +
                    " on u.teach_id=t.id " +
                    " left join subj_table s " +
                    " on u.subject_id=s.id where 1=1";
            if (name != null && !name.trim().isEmpty()) {
                sql += " and u.name=?";
            }
            if (surname != null && !surname.trim().isEmpty()) {
                sql += " and u.surname=?";
            }
            if (email != null && !email.trim().isEmpty()) {
                sql += " and u.email=?";
            }
            if (password != null && !password.trim().isEmpty()) {
                sql += " and u.password=?";
            }
            PreparedStatement stmt = c.prepareStatement(sql);
            int i = 1;
            if (name != null && !name.trim().isEmpty()) {
                stmt.setString(i, name);
                i++;
            }
            if (surname != null && !surname.trim().isEmpty()) {
                stmt.setString(i, surname);
                i++;
            }
            if (email != null && !email.trim().isEmpty()) {
                stmt.setString(i, email);
                i++;
            }
            if (password != null && !password.trim().isEmpty()) {
                stmt.setString(i, password);
                i++;
            }
            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                User u = getAllUser(rs);
                result.add(u);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public User getById(int userId) {
        User result = null;
        try (Connection c = connect()) {
            Statement stmt = c.createStatement();
            stmt.execute("SELECT u.*,t.teachway,s.subject " +
                    "FROM user u " +
                    "left join teach_table t " +
                    "on u.teach_id=t.id " +
                    "left join subj_table s " +
                    "on u.subject_id=s.id where u.id=" + userId);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                result = getAllUser(rs);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public int getIdByEmailAndPassword(String email, String password) {
        int id = 0;
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "select id from user where email=? and password=?"
            );
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    @Override
    public int getIdByEmail(String email) {
        int id = 0;
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "select id from user where email=?"
            );
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    @Override
    public User getByEmailAndPassword(String email, String password) {
        User result = null;
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "SELECT u.*,t.teachway,s.subject " +
                            "FROM user u " +
                            "left join teach_table t " +
                            "on u.teach_id=t.id " +
                            "left join subj_table s " +
                            "on u.subject_id=s.id " +
                            "where email=? and password=?"
            );
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
//                result=getSimpleAll(rs);
                result = getAllUser(rs);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public List<User> getByTeachway(String teachway) {
        List<User> result = new ArrayList<>();
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "SELECT u.*,t.teachway,s.subject" +
                            " FROM user u " +
                            " left join teach_table t " +
                            " on u.teach_id=t.id " +
                            " left join subj_table s " +
                            " on u.subject_id=s.id " +
                            " where t.teachway=?;"
            );
            stmt.setString(1, teachway);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User u = getAllUser(rs);
                result.add(u);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public List<User> getBySubject(String subject) {
        List<User> result = new ArrayList<>();
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "SELECT u.*,t.teachway,s.subject" +
                            " FROM user u " +
                            " left join teach_table t " +
                            " on u.teach_id=t.id " +
                            " left join subj_table s " +
                            " on u.subject_id=s.id " +
                            " where s.subject=?;"
            );
            stmt.setString(1, subject);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User u = getAllUser(rs);
                result.add(u);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public User getByEmail(String email) {
        User result = null;
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "SELECT u.*,t.teachway,s.subject " +
                            " FROM user u" +
                            " left join teach_table t" +
                            " on u.teach_id=t.id" +
                            " left join subj_table s" +
                            " on u.subject_id=s.id" +
                            " where email=?"
            );
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result = getAllUser(rs);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean removeUser(int userId) {
        try (Connection c = connect()) {
            Statement stmt = c.createStatement();
            stmt.execute(
                    "delete from user where id="
                            + userId);
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(User u) {
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "update user set " +
                            " name=?," +
                            " surname=?," +
                            " age=?," +
                            " university=?," +
                            " point=?," +
                            " experience=?," +
                            " teach_id=?," +
                            " subject_id=?," +
                            " code=?," +
                            " cost=?" +
                            "where id=? ");
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getSurname());
            stmt.setInt(3, u.getAge());
            stmt.setString(4, u.getUniversity());
            stmt.setInt(5, u.getPoint());
            stmt.setString(6, u.getExperience());
            stmt.setInt(7, u.getTeachwayId());
            stmt.setInt(8, u.getSubjectId());
            stmt.setString(9, u.getCode());
            stmt.setDouble(10, u.getCost());
            stmt.setInt(11, u.getId());

            return stmt.execute();


        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static BCrypt.Hasher crypt = BCrypt.withDefaults();

    @Override
    public boolean updatePassword(User u) {
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "update user set " +
                            "password=?" +
                            "where id=?");
            stmt.setString(1, crypt.hashToString(4, u.getPassword().toCharArray()));
            stmt.setInt(2, u.getId());
            return stmt.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean addUser(User u) {
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "insert into user(name,surname,age,university,point,experience,teach_id,subject_id,email,code,cost,password) " +
                            "values(?,?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getSurname());
            stmt.setInt(3, u.getAge());
            stmt.setString(4, u.getUniversity());
            stmt.setInt(5, u.getPoint());
            stmt.setString(6, u.getExperience());
            stmt.setInt(7, u.getTeachwayId());
            stmt.setInt(8, u.getSubjectId());
            stmt.setString(9, u.getEmail());
            stmt.setString(10, u.getCode());
            stmt.setDouble(11, u.getCost());
            stmt.setString(12, crypt.hashToString(4, u.getPassword().toCharArray()));
            return stmt.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean signUpUser(User u) {
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement(
                    "insert into user(name,surname,email,password) " +
                            "values(?,?,?,?)");
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getSurname());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, crypt.hashToString(4, u.getPassword().toCharArray()));
            return stmt.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
