package controllers;

import models.User;
import java.util.List;
import java.util.ArrayList;
import play.data.validation.ValidationError;

public class UserForm {

    public String username;
    public String password;
    
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (User.authenticate(username, password) == null) {
          	errors.add(new ValidationError("unauthenticate", "Incorrect Username/Password"));
        }
        return errors.isEmpty() ? null : errors;
    }
}