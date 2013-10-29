package com.cdf.admin.entity;
import com.cdf.admin.service.UserModelService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class UserModelDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<UserModel> data;

	@Autowired
    UserModelService userModelService;

	public UserModel getNewTransientUserModel(int index) {
        UserModel obj = new UserModel();
        setEmail(obj, index);
        setFirstName(obj, index);
        setLastName(obj, index);
        setName(obj, index);
        setPassword(obj, index);
        return obj;
    }

	public void setEmail(UserModel obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }

	public void setFirstName(UserModel obj, int index) {
        String firstName = "firstName_" + index;
        if (firstName.length() > 20) {
            firstName = firstName.substring(0, 20);
        }
        obj.setFirstName(firstName);
    }

	public void setLastName(UserModel obj, int index) {
        String lastName = "lastName_" + index;
        if (lastName.length() > 20) {
            lastName = lastName.substring(0, 20);
        }
        obj.setLastName(lastName);
    }

	public void setName(UserModel obj, int index) {
        String name = "name_" + index;
        if (name.length() > 15) {
            name = new Random().nextInt(10) + name.substring(1, 15);
        }
        obj.setName(name);
    }

	public void setPassword(UserModel obj, int index) {
        String password = "password_" + index;
        if (password.length() > 15) {
            password = password.substring(0, 15);
        }
        obj.setPassword(password);
    }

	public UserModel getSpecificUserModel(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UserModel obj = data.get(index);
        Long id = obj.getId();
        return userModelService.findUserModel(id);
    }

	public UserModel getRandomUserModel() {
        init();
        UserModel obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return userModelService.findUserModel(id);
    }

	public boolean modifyUserModel(UserModel obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = userModelService.findUserModelEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UserModel' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UserModel>();
        for (int i = 0; i < 10; i++) {
            UserModel obj = getNewTransientUserModel(i);
            try {
                userModelService.saveUserModel(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
