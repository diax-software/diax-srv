package me.diax.srv.client;

import me.diax.srv.stubs.model.Profile;
import me.diax.srv.stubs.service.ServiceException;

public class Main {

    public static void main(String[] args) {
        ProfileServiceClient client = new ProfileServiceClient("http://localhost:8080/");
        try {
            Profile profile = new Profile();
            profile.setId(37L);
            client.save(profile);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
