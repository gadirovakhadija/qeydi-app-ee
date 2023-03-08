package org.example.main;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.dao.impl.SubjectDaoImpl;
import org.example.dao.impl.TeachwayDaoImpl;
import org.example.dao.impl.UserDaoImpl;
import org.example.dao.inter.SubjectDaoInter;
import org.example.dao.inter.TeachwayDaoInter;
import org.example.dao.inter.UserDaoInter;
import org.example.entity.User;

import java.util.Arrays;

public class Context {
    public static UserDaoInter instanceUserDao(){
        return new UserDaoImpl();
    }
    public static TeachwayDaoInter instanceTeacwayDao(){
        return new TeachwayDaoImpl();
    }
    public static SubjectDaoInter instanceSubjectDao(){
        return new SubjectDaoImpl();
    }
}
