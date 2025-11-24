package tp.smartcard;

public class AtrParser {
    public static void main(String[] args) {
// ATR simplifié (exemple)
        String atrHex = "3B 95 11 00 00 73 C8 40 13 00";
// Découper la chaîne en octets
        String[] parts = atrHex.split("\\s+");
        byte[] atr = new byte[parts.length];
        for (int i = 0; i < parts.length; i++) {
            atr[i] = (byte) Integer.parseInt(parts[i], 16);
        }
// Appeler la méthode d'analyse
        parseAndPrint(atr);
    }

    /**
     * Analyse un ATR simplifié et affiche TS, T0,
     * les octets d'interface et les octets historiques.
     */
    public static void parseAndPrint(byte[] atr) {
        if (atr == null || atr.length < 2) {
            System.out.println("ATR trop court !");
            return;
        }
        int index = 0;
        byte TS = atr[index++];
        byte T0 = atr[index++];
// partie basse (4 bits) = nb d'octets historiques
        int k = T0 & 0x0F;
// partie haute (4 bits) = présence de TA1/TB1/TC1/TD1
        int y = (T0 & 0xF0) >> 4;
        Byte TA1 = null, TB1 = null, TC1 = null, TD1 = null;
        if ((y & 0x1) != 0 && index < atr.length) TA1 = atr[index++];
        if ((y & 0x2) != 0 && index < atr.length) TB1 = atr[index++];
        if ((y & 0x4) != 0 && index < atr.length) TC1 = atr[index++];
        if ((y & 0x8) != 0 && index < atr.length) TD1 = atr[index++];
        int remaining = atr.length - index;
        int histLen = Math.min(k, remaining);
        byte[] historical = new byte[histLen];
        System.arraycopy(atr, index, historical, 0, histLen);
// --- Affichage lisible ---
        System.out.printf("TS = %02X%n", TS);
        System.out.printf("T0 = %02X%n", T0);
        if (TA1 != null) System.out.printf("TA1 = %02X%n", TA1);
        if (TB1 != null) System.out.printf("TB1 = %02X%n", TB1);
        if (TC1 != null) System.out.printf("TC1 = %02X%n", TC1);
        if (TD1 != null) System.out.printf("TD1 = %02X%n", TD1);
        System.out.print("Octets historiques (" + histLen + ") : ");
        for (byte b : historical) {
            System.out.printf("%02X ", b);
        }
        System.out.println();
    }

}
