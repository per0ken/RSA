import java.math.BigInteger;
import java.security.SecureRandom;

public class Main {

    public static final int bitsize1 = 512;
    public static final int bitsize2 = 1024;
    public static final BigInteger e = new BigInteger("65537");

    //Probable prime egy Miller-Rabin implementáció std java libraryben
    public static BigInteger randomPrimeNumber(int size) {
        return BigInteger.probablePrime(size, new SecureRandom());
    }

    //BigIntegerek szorzása
    public static BigInteger multiplyingNumbers(BigInteger a, BigInteger b){
        return a.multiply(b);
    }


    //phiN számolása ((a-1)*(b-1))
    public static BigInteger phi(BigInteger a, BigInteger b) {
        BigInteger phiN = a.subtract(BigInteger.ONE);
        phiN = phiN.multiply(b.subtract(BigInteger.ONE));
        return phiN;
    }


    //Normál legnagyobb közös osztó kiszámítása rekurzívan
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if(b.equals(BigInteger.ZERO)){
            return a;
        }
        return gcd(b, a.mod(b));
    }

    //Kiszámoljuk a legnagyobb közös osztót, illetve az x,y értéket a gcd(a,b) = ax + by segítségével
    public static BigInteger [] extendedEuclidean (BigInteger a, BigInteger b){
        BigInteger [] gcdxy = new BigInteger[3];
        BigInteger ax, by;

        if (b.equals(BigInteger.ZERO)) {
            gcdxy[0] = a;
            gcdxy[1] = BigInteger.ONE;
            gcdxy[2] = BigInteger.ZERO;
            return gcdxy;
        }

        gcdxy = extendedEuclidean(b, a.mod (b));
        ax = gcdxy[1];
        by = gcdxy[2];
        gcdxy[1] = by;
        BigInteger temp = a.divide(b);
        temp = by.multiply(temp);
        gcdxy[2] = ax.subtract(temp);
        return gcdxy;
    }

    public static BigInteger inverse (BigInteger a, BigInteger n){
        BigInteger [] ans = extendedEuclidean(a,n);

        if (ans[1].compareTo(BigInteger.ZERO) == 1)
            return ans[1];
        else return ans[1].add(n);
    }

    public static BigInteger chineseRemainderTheorem(BigInteger d, BigInteger p, BigInteger q, BigInteger m){
        BigInteger dp, dq, qInverse, m1, m2, h;

        dp = d.mod(p.subtract(BigInteger.ONE));
        dq = d.mod(q.subtract(BigInteger.ONE));
        qInverse = inverse(q,p);

        m1 = m.modPow(dp,p);
        m2 = m.modPow(dq,q);
        h = qInverse.multiply(m1.subtract(m2)).mod(p);
        m = m2.add(h.multiply(q));

        return m;
    }


    public static BigInteger encrypt(BigInteger d, BigInteger p, BigInteger q,BigInteger m){
        return chineseRemainderTheorem(d,p,q,m);
    }
    public static BigInteger decrypt(BigInteger e, BigInteger p, BigInteger q,BigInteger m){
        return chineseRemainderTheorem(e,p,q,m);
    }

    public static void main(String args[]){

        BigInteger p, q, n, phiN, d;
        while(true) {
            p = randomPrimeNumber(bitsize2);
            q = randomPrimeNumber(bitsize2);
            n = multiplyingNumbers(p,q);
            phiN = phi(p,q);
            if (gcd(e,phiN).equals(BigInteger.ONE)) {
                break;
            }
        }

        d = inverse(e,phiN);

        BigInteger message = new BigInteger("56");
        BigInteger encryptedmessage = new BigInteger(encrypt(d, p, q, message).toString());
        BigInteger decryptedmessage = new BigInteger(encrypt(e, p, q, encryptedmessage).toString());


        System.out.println("p = "+p);
        System.out.println("q = "+q);
        System.out.println("n = "+n);
        System.out.println("phiN = "+phiN);
        System.out.println("e = "+e);
        System.out.println("d = "+d);
        System.out.println("message = "+message);
        System.out.println("Encrypted message = "+encryptedmessage);
        System.out.println("Decrypted message = "+decryptedmessage);

    }
}
