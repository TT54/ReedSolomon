import java.util.Random;

public class ReedSolomon {

    private static final Random random = new Random();

    public static void test() {
        long time = 0;

        System.out.println(Encoder.getGenerator());
        System.out.println(Encoder.getX2t());
        time = System.currentTimeMillis();
        Polynome256 toSend = Encoder.getTransmission(Encoder.getTestInformationPolynome());

        Polynome256 initial = toSend.clone();

        System.out.println(toSend);
        System.out.println("Temps de génération du polynome : " + (System.currentTimeMillis() - time) + "ms");

        time = System.currentTimeMillis();

        //System.out.println(toSend.getDegree());
        //toSend.setCoeff(48, F256.getElement(5));
        toSend.setCoeff(0, F256.getElement(8));
        toSend.setCoeff(3, F256.getElement(135));
        toSend.setCoeff(4, F256.getElement(12));

        Polynome256 decoded = Decoder.decode(toSend);
        System.out.println(decoded);
        System.out.println("Temps de récupération du message : " + (System.currentTimeMillis() - time) + "ms");


        System.out.println(decoded.equal(initial));
    }


    public static Polynome256 getRandomF256Polynome() {
        Polynome256 poly = new Polynome256(F256.getZero());
        int degree = random.nextInt(239);

        for (int i = 0; i <= degree; i++) {
            poly.setCoeff(i, F256.getElement(random.nextInt(256)));
        }

        return poly;
    }

    public static boolean testWithRandom() {
        Polynome256 generated = getRandomF256Polynome();

        Polynome256 toSend = Encoder.getTransmission(generated);
        Polynome256 toSendCopy = toSend.clone();

        int errorsAmount = random.nextInt(Encoder.t + 1);
        for (int i = 0; i < errorsAmount; i++) {
            toSend.setCoeff(random.nextInt(toSend.getDegree() + 1), F256.getElement(random.nextInt(256)));
        }

        Polynome256 decoded = Decoder.decode(toSend);

        if (decoded == null) {
            return false;
        }

        return toSendCopy.equal(decoded);
    }


    public static Polynome256 encode(Polynome256 message) {
        return Encoder.getTransmission(message);
    }

    public static Polynome256 decode(Polynome256 received) {
        return Decoder.decode(received);
    }

}
