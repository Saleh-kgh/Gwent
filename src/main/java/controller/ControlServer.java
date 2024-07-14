package controller;

import Model.DataSaver;

public class ControlServer extends Thread {
    private RegisterController registerController = new RegisterController();
    private LoginController loginController = new LoginController();
    private MainController mainController = new MainController();
    private ProfileController profileController = new ProfileController();
    private static DataSaver dataSaver = new DataSaver();

    @Override
    public synchronized void run() {
        // TODO server
    }

    public RegisterController getRegisterController() {
        return registerController;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public ProfileController getProfileController() {
        return profileController;
    }
}
