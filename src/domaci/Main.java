package domaci;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {


    private static int brojStudenata = 13;

    private static int brojNeocenjenih = 0;

    private static StringBuilder neocenjeni = new StringBuilder();

    private static float zbirOcena = 0;


    private static float prosecnaOcena;

    private static int brojOcenjenihStudenata = 0;

    private static Stack<Student> studentiStack;

    private static ArrayList<Student> asistentoviStudenti = new ArrayList<>();
    private static ArrayList<Student> profesorovistudenti = new ArrayList<>();


    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    private static Semaphore semaphore = new Semaphore(1);;


    public static void main(String[] args) {


        studentiStack = new Stack<>();
        Random r = new Random();

        List<Student> studenti = new ArrayList<>();

        for(int i=0; i<brojStudenata; i++){
            studenti.add(new Student(i+1));
        }
        Collections.sort(studenti);


        studentiStack.addAll(studenti);

        /*for(Student s: studentiStack){
            System.out.println(s);
        }*/


            boolean neparnaIteracija = true;
            while(studentiStack.size()>2){
                if(neparnaIteracija){
                    profesorovistudenti.add(studentiStack.pop());
                }else{
                    asistentoviStudenti.add(studentiStack.pop());
                }
                neparnaIteracija = !neparnaIteracija;
            }


        if(studentiStack.size() == 2 ){
            profesorovistudenti.add(studentiStack.pop());
        }

            if(studentiStack.size() == 1){
                asistentoviStudenti.add(studentiStack.pop());
            }




        ArrayList<Thread> zapocetiTredovi = new ArrayList<>();


        long start = System.currentTimeMillis() + r.nextInt(1000) + 5000;
        long stop = start + 5000;


        do {
            if (System.currentTimeMillis() > start) {
                if (!profesorovistudenti.isEmpty()) {
                    if (start + profesorovistudenti.get(0).getVremeDolaskaNaOdbranu() < System.currentTimeMillis()
                            ) {
                        Student student = profesorovistudenti.get(0);
                        profesorovistudenti.remove(0);

                        Thread newThread = new Thread(new Profesor(cyclicBarrier, student, start));
                        zapocetiTredovi.add(newThread);
                        newThread.start();
                    }
                }

                if (!asistentoviStudenti.isEmpty()) {
                    if (start + asistentoviStudenti.get(0).getVremeDolaskaNaOdbranu() < System.currentTimeMillis()) {
                        Student student = asistentoviStudenti.get(0);
                        asistentoviStudenti.remove(0);

                        Thread newThread = new Thread(new Asistent(semaphore, student, start));
                        zapocetiTredovi.add(newThread);
                        newThread.start();
                    }
                }
            }

        } while (System.currentTimeMillis() <= stop);

     
        zapocetiTredovi.forEach(Thread::interrupt);

        System.out.println("Prosek ocena: "+ prosecnaOcena+ " . Broj studenata koji su stigli da odbrane: " + brojOcenjenihStudenata + "."
                );

        System.out.println("Broj neocenjenih: " + brojNeocenjenih);

        System.out.println(neocenjeni.toString());


        System.out.println("Pocetak: " +  start + " | Kraj: " + stop + "  | Vreme trajanje: " + (System.currentTimeMillis() - start) );

    }







    public static synchronized void dodatiOcenuStudenta(int ocena) {
            zbirOcena += ocena;
            brojOcenjenihStudenata++;
            prosecnaOcena = zbirOcena / (float) brojOcenjenihStudenata;

    }


    public static synchronized void brojNeocenjenih() {
        brojNeocenjenih++;

    }

    public static synchronized void neocenjeni(Student s){

        neocenjeni.append(s.getName() + " | ");

    }

}
