package epaw.lab4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import epaw.lab4.model.User;
import epaw.lab4.repository.UserRepository;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UserService {

    private static UserService instance;
    private UserRepository userRepository;

    private UserService() {
        this.userRepository = UserRepository.getInstance();
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_REGEX = "^[0-9]{9}$";

    public Map<String, String> validate(User user) {
        Map<String, String> errors = new HashMap<>();

        String name = user.getName();
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Username cannot be empty.");
        } else if (name.length() < 5 || name.length() > 20) {
            errors.put("name", "Username must be between 5 and 20 characters.");
        } else if (userRepository.existsByUsername(name)) {
            errors.put("name", "Username already exists.");
        }

        String password = user.getPassword();
        if (password == null || !password.matches(PASSWORD_REGEX)) {
            errors.put("password",
                    "Minimum 8 characters, including uppercase, numbers, and a special character (@#$%^&*).");
        }

        String confirmPassword = user.getConfirmPassword();
        if (confirmPassword == null || !confirmPassword.equals(password)) {
            errors.put("confirmPassword", "Passwords do not match.");
        }

        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email cannot be empty.");
        } else if (email.length() > 120 || !email.matches(EMAIL_REGEX)) {
            errors.put("email", "Enter a valid email address.");
        } else if (userRepository.existsByEmail(email.trim())) {
            errors.put("email", "Email already exists.");
        }

        String birthDate = user.getBirthDate();
        if (birthDate == null || birthDate.trim().isEmpty()) {
            errors.put("birthDate", "Birth date is required.");
        }

        String phone = user.getPhone();
        if (phone != null && !phone.trim().isEmpty()) {
            String cleanPhone = phone.trim();
            if (!cleanPhone.matches(PHONE_REGEX)) {
                errors.put("phone", "Phone must contain 9 digits.");
            } else if (userRepository.existsByPhone(cleanPhone)) {
                errors.put("phone", "Phone already exists.");
            }
        }

        return errors;
    }

    private void normalizePhone(User user) {
        String phone = user.getPhone();
        if (phone == null) {
            return;
        }

        String cleanPhone = phone.trim();
        user.setPhone(cleanPhone.isEmpty() ? null : cleanPhone);
    }

    public Map<String, String> validateEdit(User user, boolean passwordChanged) {
        Map<String, String> errors = new HashMap<>();

        String name = user.getName();
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Username cannot be empty.");
        } else if (name.length() < 5 || name.length() > 20) {
            errors.put("name", "Username must be between 5 and 20 characters.");
        } else if (userRepository.existsByUsernameExceptId(name, user.getId())) {
            errors.put("name", "Username already exists.");
        }

        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email cannot be empty.");
        } else if (email.length() > 120 || !email.matches(EMAIL_REGEX)) {
            errors.put("email", "Enter a valid email address.");
        } else if (userRepository.existsByEmailExceptId(email.trim(), user.getId())) {
            errors.put("email", "Email already exists.");
        }

        String birthDate = user.getBirthDate();
        if (birthDate == null || birthDate.trim().isEmpty()) {
            errors.put("birthDate", "Birth date is required.");
        }

        String phone = user.getPhone();
        if (phone != null && !phone.trim().isEmpty()) {
            String cleanPhone = phone.trim();
            if (!cleanPhone.matches(PHONE_REGEX)) {
                errors.put("phone", "Phone must contain 9 digits.");
            } else if (userRepository.existsByPhoneExceptId(cleanPhone, user.getId())) {
                errors.put("phone", "Phone already exists.");
            }
        }

        String password = user.getPassword();
        if (passwordChanged) {
            if (password == null || !password.matches(PASSWORD_REGEX)) {
                errors.put("password",
                        "Minimum 8 characters, including uppercase, numbers, and a special character (@#$%^&*).");
            }
            String confirmPassword = user.getConfirmPassword();
            if (confirmPassword == null || !confirmPassword.equals(password)) {
                errors.put("confirmPassword", "Passwords do not match.");
            }
        }

        return errors;
    }

    public Map<String, String> register(User user) {
        normalizePhone(user);
        if (user.getRole() == null) {
            user.setRole("REGULAR");
        }
        Map<String, String> errors = validate(user);
        if (errors.isEmpty()) {
            userRepository.save(user);
        }
        return errors;
    }

    public Map<String, String> updateProfile(User user, boolean passwordChanged) {
        normalizePhone(user);
        Map<String, String> errors = validateEdit(user, passwordChanged);
        if (errors.isEmpty()) {
            userRepository.update(user);
        }
        return errors;
    }

    public Map<String, String> login(User user) {
        Map<String, String> errors = new HashMap<>();
        if (!userRepository.checkLogin(user)) {
            errors.put("password", "The combination of name and password does not match in our dataabase");
        }
        return errors;
    }

        // Get all users
    public List<User> getAllUsers() {
    	Optional<List<User>> users = userRepository.findAll();
    	if (users.isPresent())
    	    return users.get();
        return null;
    }

    // Get a user by id
    public User getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent())
            return user.get();
        return null;
    }
    
    // Get followed users
    public List<User> getFollowedUsers(Integer id, Integer start, Integer end) {
    	Optional<List<User>> users = userRepository.findFollowed(id,start,end);
    	if (users.isPresent())
    	    return users.get();
        return null;
    }
    
    // Get unfollowed users
    public List<User> getNotFollowedUsers(Integer id, Integer start, Integer end) {
    	Optional<List<User>> users = userRepository.findNotFollowed(id,start,end);
    	if (users.isPresent())
    	    return users.get();
        return null;
    }
    
    // Follow User
    public void follow(Integer uid,Integer fid) {
    	userRepository.followUser(uid, fid);
    }
    
    // Unfollow User
    public void unfollow(Integer uid,Integer fid) {
    	userRepository.unfollowUser(uid, fid);
    }

    // Get followers of a user
    public List<User> getFollowersUsers(Integer id, Integer start, Integer end) {
        Optional<List<User>> users = userRepository.findFollowers(id,start,end);
        if (users.isPresent())
            return users.get();
        return null;
    }

    // Delete a user account
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public Map<String, String> updateUser(User user, boolean passwordChanged) {
        return updateProfile(user, passwordChanged);
    }

    public String saveProfilePicture(Part filePart, String username) {
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }

        try {
            String fileName = filePart.getSubmittedFileName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = username + extension;

            String resourcesDir = "EXTERNAL_RESOURCES";
            Files.createDirectories(Paths.get(resourcesDir));

            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, Paths.get(resourcesDir, newFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}