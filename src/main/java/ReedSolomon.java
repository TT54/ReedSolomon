import javax.annotation.processing.SupportedSourceVersion;

public class ReedSolomon {

    public static void test(){
        long time = 0;

        System.out.println(Encoder.getGenerator());
        System.out.println(Encoder.getX2t());
        time = System.currentTimeMillis();
        Polynome256 toSend = Encoder.getTransmission(Encoder.getTestInformationPolynome());
        System.out.println(toSend);
        System.out.println("Temps de génération du polynome : " + (System.currentTimeMillis() - time) + "ms");

        time = System.currentTimeMillis();

        //System.out.println(toSend.getDegree());
        //toSend.setCoeff(48, F256.getElement(5));
        toSend.setCoeff(0, F256.getElement(5));
        toSend.setCoeff(3, F256.getElement(5));

        Polynome256 decoded = Decoder.decode(toSend);
        System.out.println(decoded);
        System.out.println("Temps de récupération du message : " + (System.currentTimeMillis() - time) + "ms");
    }

}
