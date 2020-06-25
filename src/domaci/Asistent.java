package domaci;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Asistent implements Runnable {

    private Semaphore semaphore;

    private Student student;

    private long start;

    public Asistent(Semaphore semaphore, Student student, long start) {
        this.semaphore = semaphore;
        this.student = student;
        this.start = start;
    }

    @Override
    public void run() {


        student.setZapoceo(true);

        System.out.println("ASISTENT: "
                + student.getName() + " je dosao na red. Vreme: " +  (System.currentTimeMillis() - start));

        try {
            semaphore.acquire();



                System.out.println("ASISTENT: "
                        + student.getName() + " je usao na propitivanje. Vreme: " + (System.currentTimeMillis() - start)
                        + ": Vreme potrebno sza odbranu" + student.getVremePotrebnoZaOdbranu());
                Thread.sleep(student.getVremePotrebnoZaOdbranu());

                Random random = new Random();
                int ocena = random.nextInt(10) + 1;
                student.setOcena(ocena);
                Main.dodatiOcenuStudenta(ocena);

                System.out.println("!ASISTENT: "
                        + student.getName() + ", je zavrsio sa propitivanjem, ocena: "
                        + student.getOcena() + ". Vreme: " +(System.currentTimeMillis() - start));


            semaphore.release();
        } catch (InterruptedException e) {
            if(student.isZapoceo()){
                Random random = new Random();
                int ocena = random.nextInt(10) + 1;
                student.setOcena(ocena);
                Main.dodatiOcenuStudenta(ocena);
                System.out.println("*ASISTENT: Student sa id: "
                        + student.getId() + ", je zavrsio sa propitivanjem, ocena: "
                        + student.getOcena() + ". Vreme: " +(System.currentTimeMillis() - start));
            }else{
                Main.brojNeocenjenih();
                Main.neocenjeni(student);
            }


        }
    }
}
