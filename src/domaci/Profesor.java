package domaci;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Profesor implements  Runnable{

    private CyclicBarrier barrier;

    private Semaphore semaphore;

    private Student student;

    private long start;


    public Profesor(CyclicBarrier barrier, Student student, long start) {
        this.barrier = barrier;
        this.student = student;
        this.start = start;
        semaphore = new Semaphore(2);
    }


    @Override
    public void run() {

        student.setZapoceo(true);

        System.out.println("PROFESOR: Student "
                + student.getName() + ", je dosao na red. Vreme: " + System.currentTimeMillis()
                + " Razlika od starta: " + (System.currentTimeMillis() - start));

        try {
            this.semaphore.acquire();
            barrier.await();



                float pom = System.currentTimeMillis() - start;
                System.out.println("PROFESOR: Student"
                        + student.getName() + ", je usao na propitivanje. Vreme: " + pom
                        + ":" + student.getVremePotrebnoZaOdbranu());

                Thread.sleep(student.getVremePotrebnoZaOdbranu());

                Random random = new Random();
                int ocena = random.nextInt(11);
                student.setOcena(ocena);
                Main.dodatiOcenuStudenta(ocena);


                System.out.println("!PROFESOR: Student "
                        + student.getName() + ", je zavrsio sa propitivanjem, ocena: "
                        + student.getOcena() + ". Vreme: " + (System.currentTimeMillis() - start)+  "     " + pom + "     " + student.getVremePotrebnoZaOdbranu() );

            semaphore.release();
        } catch (InterruptedException e) {
            if(student.isZapoceo()){
                Random random = new Random();
                int ocena = random.nextInt(10) + 1;
                student.setOcena(ocena);
                Main.dodatiOcenuStudenta(ocena);
                System.out.println("*PROFESOR: Student sa id: "
                        + student.getId() + ", je zavrsio sa propitivanjem, ocena: "
                        + student.getOcena() + ". Vreme: " +(System.currentTimeMillis() - start));
            }else{
                Main.brojNeocenjenih();
                Main.neocenjeni(student);
            }

        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
