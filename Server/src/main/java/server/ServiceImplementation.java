package server;

import joc.Joc;
import joc.Jucator;
import joc.Propunere;
import joc.persistance.JocRepository;
import joc.persistance.JucatorRepository;
import joc.persistance.PropunereRepository;
import services.IObserver;
import services.IServices;
import services.MyException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementation implements IServices {

    private JucatorRepository jucatorRepository;
    private JocRepository jocRepository;
    private PropunereRepository propunereRepository;
    private Map<String, IObserver> usersLogati;
    //START
    private Map<String, List<Integer>> readyToPlay = new HashMap<>(); // {username -> [poz1,poz2,poz3]}
    private boolean hasStarted = false;
    private Map<String, String> cuvinteSelectate = new HashMap<>(); // {username -> "_________"}
    private int gameId = 0;
    private int nrRunda = 1;
    //PROPUNERE
    private Map<String, List<String>> choices = new HashMap<>();//       {username -> [jucatorPropus, pozitiePropusa]}


    public ServiceImplementation(JucatorRepository jucatorRepository, JocRepository jocRepository, PropunereRepository propunereRepository) {
        this.jucatorRepository = jucatorRepository;
        this.jocRepository = jocRepository;
        this.propunereRepository = propunereRepository;
        usersLogati = new ConcurrentHashMap<>();
    }

    @Override
    public Jucator login(String username, String parola, IObserver obs) throws MyException {
        Jucator jucator1 = jucatorRepository.findUserByUsernameParola(username,
                parola);

        if (jucator1 != null) {
            if (usersLogati.get(jucator1.getId()) != null) {
                throw new MyException("Userul deja s-a logat!");
            }
            usersLogati.put(jucator1.getId(), obs);

        } else {
            throw new MyException("Autentificare esuata!");
        }
        return jucator1;
    }

    @Override
    public synchronized void logout(Jucator jucator, IObserver obs) throws MyException {

        usersLogati.remove(jucator.getId());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void start(Jucator player, int pozitie1, int pozitie2, int pozitie3) {
        if (!readyToPlay.keySet().contains(player.getId()) && !hasStarted) {
            List<Integer> pozitii = new ArrayList<>();
            pozitii.add(pozitie1);
            pozitii.add(pozitie2);
            pozitii.add(pozitie3);
            readyToPlay.put(player.getId(), pozitii);
            cuvinteSelectate.put(player.getId(), "_________");
        }
        if (readyToPlay.size() == 3) {
            int idJoc = getNextGame();
            hasStarted = true;
            for (String jucator : readyToPlay.keySet()) {

                Joc joc = new Joc();
                joc.setIdJoc(idJoc);
                joc.setUsername(jucator);
                joc.setPoz1(readyToPlay.get(jucator).get(0)); //poz1
                joc.setPoz2(readyToPlay.get(jucator).get(1)); //poz2
                joc.setPoz3(readyToPlay.get(jucator).get(2)); //poz3

                jocRepository.save(joc);
                notifyNewGame(jucator);
            }
        }
    }

    private int getNextGame() {
        int maxId = 0;
        Iterable<Joc> games = jocRepository.findAll();
        for (Joc joc : games)
            if (joc.getIdJoc() > maxId)
                maxId = joc.getIdJoc();
        gameId = maxId + 1;
        return maxId + 1;
    }

    private final int defaultThreadsNo = 5;

    private void notifyNewGame(String jucator) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (String str : usersLogati.keySet()) {
            if (readyToPlay.containsKey(str) && str.equals(jucator)) {
                IObserver iObserver = usersLogati.get(str);
                if (iObserver != null)
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying " + str + " new game");
                            //vrem ca in controller sa avem lista de jucatori
                            //readyToPlay.keySet() = useri
                            List<String> keys = new ArrayList<>(readyToPlay.keySet());
                            iObserver.notifyNewGame(keys);
                        } catch (RemoteException | MyException e) {
                            System.err.println("Error notifying " + e);
                        }
                    });
            }
        }
        executor.shutdown();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void joacaRunda(String jucatorConectat, String jucatorPropus, int pozitiePropusa) throws MyException {
        List<String> propunere = new ArrayList<>();
        propunere.add(jucatorPropus);
        propunere.add(String.valueOf(pozitiePropusa));
        choices.put(jucatorConectat, propunere); //choices = {jucatorConectat -> [jucatorPropus, pozitiePropusa]}

        //readyToPlay = {username -> [poz1, poz2, poz3]}

        //toata lumea a facut alegerea
        if (choices.keySet().size() == 3) {

            String textMesaj = "";
            String textScor = "";

            //parcurgem alegerile
            for (String user : choices.keySet()) {
                // pozitiaPropusa= poz1 sau pozitiaPropusa = poz2 sau pozitiePropusa = poz3 => 7p
                // choices.get(user).get(1) = pozPropusa
                // readyToPlay.get(choices.get(user).get(0)).get(0) = readuToPlay.get(jucatorPropus).get(0) = poz1
                if (Integer.parseInt(choices.get(user).get(1)) == readyToPlay.get(choices.get(user).get(0)).get(0) ||
                        Integer.parseInt(choices.get(user).get(1)) == readyToPlay.get(choices.get(user).get(0)).get(1) ||
                        Integer.parseInt(choices.get(user).get(1)) == readyToPlay.get(choices.get(user).get(0)).get(2)) {

                    textMesaj = textMesaj + user + " : 7p Obiect la poz : " + choices.get(user).get(1) + " pentru " + choices.get(user).get(0) + "\n";

                    Propunere prounereNoua = new Propunere();
                    prounereNoua.setIdJoc(gameId);
                    prounereNoua.setNrRunda(nrRunda);
                    prounereNoua.setUsername(user);
                    prounereNoua.setJucatorPropus(choices.get(user).get(0));
                    prounereNoua.setPozAleasa(Integer.parseInt(choices.get(user).get(1)));
                    prounereNoua.setNrPuncte(7);

                    propunereRepository.save(prounereNoua);



                    String oldCuvant = cuvinteSelectate.get(choices.get(user).get(0)); //"______"
                    int pozPropusa = Integer.parseInt(choices.get(user).get(1));
                    String newCuvant = oldCuvant.substring(0, pozPropusa - 1) + 'O' + oldCuvant.substring(pozPropusa);
                    cuvinteSelectate.put(choices.get(user).get(0), newCuvant); //face replace
                    System.out.println("JOACA RUNDA: " + cuvinteSelectate.get(choices.get(user).get(0)));
                } else {

                    if (Integer.parseInt(choices.get(user).get(1)) - 1 == readyToPlay.get(choices.get(user).get(0)).get(0) ||
                            Integer.parseInt(choices.get(user).get(1)) + 1 == readyToPlay.get(choices.get(user).get(0)).get(0) ||
                            Integer.parseInt(choices.get(user).get(1)) - 1  == readyToPlay.get(choices.get(user).get(0)).get(1) ||
                            Integer.parseInt(choices.get(user).get(1)) + 1  == readyToPlay.get(choices.get(user).get(0)).get(1) ||
                            Integer.parseInt(choices.get(user).get(1)) - 1  == readyToPlay.get(choices.get(user).get(0)).get(2) ||
                            Integer.parseInt(choices.get(user).get(1)) + 1  == readyToPlay.get(choices.get(user).get(0)).get(2)) {

                        textMesaj = textMesaj + user + " : 3p Vecin la poz: " + choices.get(user).get(1) + " pentru " + choices.get(user).get(0) + "\n";

                        Propunere prounereNoua = new Propunere();
                        prounereNoua.setIdJoc(gameId);
                        prounereNoua.setNrRunda(nrRunda);
                        prounereNoua.setUsername(user);
                        prounereNoua.setJucatorPropus(choices.get(user).get(0));
                        prounereNoua.setPozAleasa(Integer.parseInt(choices.get(user).get(1)));
                        prounereNoua.setNrPuncte(3);

                        propunereRepository.save(prounereNoua);



                        String oldCuvant = cuvinteSelectate.get(choices.get(user).get(0)); //"___"
                        int pozPropusa = Integer.parseInt(choices.get(user).get(1));
                        String newCuvant = oldCuvant.substring(0, pozPropusa - 1) + 'N' + oldCuvant.substring(pozPropusa);

                        cuvinteSelectate.put(choices.get(user).get(0), newCuvant); //face replace
                        System.out.println("JOACA RUNDA: " + cuvinteSelectate.get(choices.get(user).get(0)));
                    } else {

                        textMesaj = textMesaj + user + " : 0p Nimic la pozitia: " + choices.get(user).get(1) + " pentru " + choices.get(user).get(0) + "\n";

                        Propunere prounereNoua = new Propunere();
                        prounereNoua.setIdJoc(gameId);
                        prounereNoua.setNrRunda(nrRunda);
                        prounereNoua.setUsername(user);
                        prounereNoua.setJucatorPropus(choices.get(user).get(0));
                        prounereNoua.setPozAleasa(Integer.parseInt(choices.get(user).get(1)));
                        prounereNoua.setNrPuncte(0);

                        propunereRepository.save(prounereNoua);



                        String oldCuvant = cuvinteSelectate.get(choices.get(user).get(0)); //"___"
                        int pozPropusa = Integer.parseInt(choices.get(user).get(1));

                        String newCuvant = oldCuvant.substring(0, pozPropusa - 1) + 'C' + oldCuvant.substring(pozPropusa);

                        cuvinteSelectate.put(choices.get(user).get(0), newCuvant); //face replace
                        System.out.println("JOACA RUNDA: " + cuvinteSelectate.get(choices.get(user).get(0)));

                    }

                }

            }

            choices.clear();
            nrRunda++;

            if(nrRunda <= 3){
                notifyWinner(textMesaj, cuvinteSelectate);
            } else
                notifyWinner(textMesaj);


        }
    }

    private void notifyWinner(String textMesaj){
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (String user : usersLogati.keySet()) {
            if (readyToPlay.containsKey(user)) {
                IObserver iObserver = usersLogati.get(user);
                if (iObserver != null)
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying " + user + " new game");
                            iObserver.notifyWinner(textMesaj);
                        } catch (RemoteException | MyException e) {
                            System.err.println("Error notifying " + e);
                        }
                    });
            }
        }
        executor.shutdown();
    }

    private void notifyWinner(String textMesaj, Map<String,String> cuvinteSelectate){
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (String user : usersLogati.keySet()) {
            if (readyToPlay.containsKey(user)) {
                IObserver iObserver = usersLogati.get(user);
                if (iObserver != null)
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying " + user + " new game");
                            iObserver.notifyWinner(textMesaj,cuvinteSelectate);
                        } catch (RemoteException | MyException e) {
                            System.err.println("Error notifying " + e);
                        }
                    });
            }
        }
        executor.shutdown();
    }



}
