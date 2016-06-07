package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    //////////////////////////////   ZALOZENIA   /////////////////////////////

    /*
    Login:
    ma byc unikalny login i haslo, przy tworzeniu robi sie haslo "new", przy 1 loginie system wymusza zmiane hasla
    3 niepoprawne hasla i konto sie blokuje,
    mozna wystawic reset i enable, dostaniesz nowe randomowe haslo i reset prob,
    login generuje sie: Pierwsza litera typa, Pierwsza litera imienia, 3 losowe cyfry
     */

    /*
    Admin:
    tworzy i modyfikuje uzytkownikow, nie moze siebie zmienic,
    robi resety hasla i loginow,
    dodaje nowe role, userow i brancze,
    usuwa wszystko,
    niektore akcje wymagaja autoryzacji innego admina
    ale zmiana imienia nie wymaga autoryzacji
     */

    /*
    Manager:
    przypisany do brancza, przymuje i wydaje w branczu kase, robi raporty branczowe,
    duze transakcje maja byc autoryzowane przez managera
     */

    /*
    Employee:
    przypisany do brancza, dodaje i usuwa klientow i ich transakcje, robi raporty z nich,
    musi miec przypisana jakas role, limity na orgach: jest 5, kazdy odpowiada za wykonywanie transakcji o dopuszczalnym limicie
    duze transakcje maja byc autoryzowane przez managera
     */

    /*
    Role:
    kazda rola ma swoje limity transakcji
    Limity:
    1 - deposit,
    2 - withdrawal,
    3 - transfer,
    4 - payment,
    5 - online
     */

    /*
    Branch:
    ma managera, employees i klientow,
    ma okreslona sume gotowki na skladzie
     */

    /*
    Report:
    mozna wyswietlic na ekranie a potem do pliku CSV
     */


    static Program program = new Program();
    Controller controller = new Controller();
    static Admin activeAdmin;
    static Manager activeManager;
    static Employee activeEmployee;

    @Override
    public void start(Stage primaryStage) throws Exception {

        program.loadAll();

//        show log in screen
        Parent root2 = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        primaryStage.setTitle("Amber Gold");
        Scene scene = new Scene(root2);
        primaryStage.setScene(scene);
        primaryStage.show();
//        save invalid attempts
        primaryStage.setOnCloseRequest(e -> {
            program.saveUsers();
        });
    }


    public static void main(String[] args) {

        launch(args);
    }


}
