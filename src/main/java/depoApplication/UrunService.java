package depoApplication;

import java.util.*;

public class UrunService {
    private static Map<Integer, Urun> urunListesi = new HashMap<>();
    private static Set<Integer> kullanilmayanIdler = new HashSet<>();
    private static int sonId = 1004;
    private static Scanner input = new Scanner(System.in);

    public static void urunIslemMenusu ()   {

        Scanner input = new Scanner(System.in);
        //String urunIsmi, String uretici, int miktar, String birim, String rafNo
        Urun urun1 = new Urun(1001,"un","ülker",5,"çuval","001");
        Urun urun2 = new Urun(1002,"zeytinyağı", "komili", 3, "teneke", "002");
        Urun urun3 = new Urun(1003,"şeker", "karşeker", 8, "çuval", "003");
        urunListesi.put(urun1.getId(), urun1);
        urunListesi.put(urun2.getId(), urun2);
        urunListesi.put(urun3.getId(), urun3);


        // Kullanici hatali bir secim yaptiginda islem tekrari yapip duzeltmesini saglamak icin metodumuzda do while dongusunu kullandik

        int  select = -1;   //Başlangıçta geçersiz bir değer atayarak döngüyü başlatıyoruz.

        do {
            System.out.println("=====================================================================================");
            System.out.println("                    ***       DEPO URUN ISLEM MENUSU        ***                      ");
            System.out.println("=====================================================================================");


            System.out.println(" Lutfen istediginiz islemin numarasini seciniz  " +
                    "\n1) Urun Tanimlama   ==> 1   \n2) Urun Listeleme   ==> 2   \n3) Depo Urun Girisi ==> 3 " +
                    "    \n4) Urunu Rafa Koyma ==> 4   \n5) Depo Urun Cikisi ==> 5    \n6) Envanterden Sil  ==> 6  \n0) CIKIS ");

            System.out.println("Islem No :  " );


            try {                                  // Islem numarasi icin veri dogrulama
                select = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Lütfen 0 ile 6 arasinda bir sayı giriniz");

                input.nextLine(); // Geçersiz girişi atlamak için kullanılır.
                continue; // Döngünün başına dön.
            }



            System.out.println("-----------------------------------------------------------------------------------------");



            switch(select) {
                case 1 :
                    urunTanimla();
                    break;
                case 2 :
                    urunListele ();
                    break;
                case 3 :
                    urunMiktariGiris();
                    break;
                case 4 :
                    urunRafaKoy();
                    break;
                case 5 :
                    urunCikisi();
                    break;
                case 6:
                    urunSilme();
                    break;
                case 0 :
                    System.out.println("Iyi Gunler Dileriz");
                    break;
                default:
                    System.out.println("Lütfen 0 ile 6 arasinda bir sayı giriniz");
                    break;
            }


        }while(select!=0);  // Eger kullanici cikis icin 0 i secerse dongu bitiyor


        //  input.close();  // Scanner nesnesini kapat
    }


    private static int kullaniciSayiGirisiAl(String mesaj) {
        int sayi;
        do {
            try {
                System.out.println(mesaj);
                sayi = input.nextInt();
                if (sayi <= 0) {
                    System.out.println("Lütfen sıfırdan büyük bir sayı giriniz!");
                    sayi = -1; // Kullanıcı sıfır veya negatif bir sayı girdiyse, döngüyü sürdür.
                }
            } catch (InputMismatchException e) {
                System.out.println("Hata: Geçerli bir tam sayı giriniz!");
                input.nextLine(); // Geçersiz girişi temizle.
                sayi = -1; // Hatalı giriş yapıldığı için döngüyü sürdür.
            }
        } while (sayi == -1);
        return sayi;
    }


    // Kullanıcıdan metinsel giriş almak için yardımcı metod

    private static final String VALID_PATTERN = "[a-z0-9 çşğüöı-]+";
    private static final int MIN_UZUNLUK = 2;
    private static final int MAX_UZUNLUK = 20;

    private static String kullaniciMetinGirisiAl(String mesaj, String hataMesaji) {
        String giris;
        do {
            System.out.println(mesaj);
            giris = input.nextLine().trim().toLowerCase();
            if (!isValidProductInput(giris)) {
                System.out.println(hataMesaji);
            }
        } while (!isValidProductInput(giris));
        return giris;
    }

    private static boolean isValidProductInput(String input) {
        return input.matches(VALID_PATTERN) &&
                input.length() >= MIN_UZUNLUK &&
                input.length() <= MAX_UZUNLUK;
    }

    // Id olusturma

    private static int YeniIdOlustur() {
        if (kullanilmayanIdler.isEmpty()) {
            return sonId++;
        } else {
            int minKullanilmayanId = kullanilmayanIdler.stream().min(Integer::compareTo).get();
            kullanilmayanIdler.remove(minKullanilmayanId);
            return minKullanilmayanId;
        }
    }

    // Urun tanimlama

    public static void urunTanimla() {
        System.out.println("*** URUN TANIMLAMA ***");
// Urun özellikleri alınıyor
        String isim = kullaniciMetinGirisiAl("ürünün ismini giriniz: ", "ürun ismi 2-20 karakterden olusan harf ve rakamlar icermelidir.");
        String uretici = kullaniciMetinGirisiAl("üreticisini giriniz:", "üretici ismi 2-20 karakterden olusan harf ve rakamlar icermelidir.");
        String birim = kullaniciMetinGirisiAl("birimini giriniz: (cuval,koli,teneke)", "birim ismi 2-20 karakterden olusan harf ve rakamlar icermelidir.");

        // Yeni ürün mevcut degilse yeni urun  oluşturuluyor ve listeye ekleniyor

        boolean urunMevcut = false;

        for (Urun urun : urunListesi.values()) {
            if (urun.getUrunIsmi().equalsIgnoreCase(isim) &&
                    urun.getUretici().equalsIgnoreCase(uretici) &&
                    urun.getBirim().equalsIgnoreCase(birim)) {
                System.out.println("Bu isim ve üretici ile zaten bir ürün mevcut. Ürün ID'si: " + urun.getId());
                urunMevcut = true;
                break;
            }
        }

        if (!urunMevcut) {
            Urun yeniUrun = new Urun(YeniIdOlustur(), isim, uretici, 0, birim, null);
            urunListesi.put(yeniUrun.getId(), yeniUrun);
            System.out.println("Yeni ürün başarıyla eklendi. Ürün ID'si: " + yeniUrun.getId());
        }
    }

    // Urun listeleme

    public static void urunListele() {

        if (urunListesi.isEmpty()) {
            System.out.println("Depoda hiç ürün bulunmamaktadır.");
            //    return;
        }

        System.out.println("                              *** URUN LISTESI ***                                        ");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-18s %-20s %-15s %-15s %-10s\n","ID", "İsmi", "Üreticisi", "Miktarı", "Birimi", "Raf");
        System.out.println("-----------------------------------------------------------------------------------------");

        urunListesi.values().forEach(urun -> System.out.printf("%-10d %-18s %-20s %-15d %-15s %-10s\n",
                urun.getId(),
                urun.getUrunIsmi(),
                urun.getUretici(),
                urun.getMiktar(),
                urun.getBirim(),
                (urun.getRafNo() == null) ? "-" : urun.getRafNo()));

        System.out.println("-----------------------------------------------------------------------------------------");
    }

    // Urun bulma

    public static Urun urunBul(int id) {

        return urunListesi.get(id);
    }


    // Urun miktari girisi

    public static void urunMiktariGiris() {
        System.out.println("*** URUN GIRISI ***");
        int id = kullaniciSayiGirisiAl("Ürün ID'sini giriniz:");
        Urun urun = urunBul(id);

        if (urun != null) {
            int eklenenMiktar = kullaniciSayiGirisiAl("Eklemek istediğiniz miktarı giriniz (0'dan büyük bir değer olmalı):");

            urun.setMiktar(urun.getMiktar() + eklenenMiktar);
            System.out.println("Urun girişi başarılı. Eklenen miktar: " + eklenenMiktar + " " + urun.getBirim() +
                    ". Yeni miktar: " + urun.getMiktar() + " " + urun.getBirim());
        } else {
            System.out.println("Hata: Bu ID'ye sahip bir ürün bulunamadı!");
        }
    }

    // Urunu rafa koyma

    public static void urunRafaKoy() {
        System.out.println("*** URUNU RAFA KOY ***");
        int id = kullaniciSayiGirisiAl("Urun ID'sini giriniz: ");
        Urun urun = urunBul(id);
        if (urun != null) {
            String idStr = String.valueOf(id);
            String rafNo = idStr.length() > 3 ? idStr.substring(idStr.length() - 3) : idStr; // ID'nin son üç rakamını alır veya ID yeterince uzun değilse tümünü alır.
            urun.setRafNo(rafNo);
            System.out.println("Urun rafa yerleştirildi: Raf No " + rafNo);
        } else {
            System.out.println("Hata: Bu ID'ye sahip bir ürün bulunamadı.");
        }

    }

    // Urun cikisi

    public static void urunCikisi() {
        System.out.println("*** URUN CIKISI ***");
        int id = kullaniciSayiGirisiAl("Urun ID'sini giriniz:");
        Urun urun = urunBul(id);

        if (urun != null) {
            int cikarilanMiktar = kullaniciSayiGirisiAl("Cikarmak istediğiniz miktarı giriniz (0'dan büyük bir değer olmalı):");

            if (urun.getMiktar() >= cikarilanMiktar) {
                urun.setMiktar(urun.getMiktar() - cikarilanMiktar);
                System.out.println("Urun çıkışı başarılı. Çıkarılan miktar: " + cikarilanMiktar + " " + urun.getBirim() +
                        ". Kalan miktar: " + urun.getMiktar() + " " + urun.getBirim());
            } else {
                System.out.println("Yetersiz stok. Mevcut miktar: " + urun.getMiktar() + " " + urun.getBirim());      //Stok kontrolu
            }
        } else {
            System.out.println("Hata: Bu ID'ye sahip bir ürün bulunamadı!");
        }
    }

    // Urun silme

    public static void urunSilme() {
        System.out.println("*** URUN SILME ***");
        int id = kullaniciSayiGirisiAl("Urun ID'sini giriniz:");
        Urun urun = urunBul(id);

        if (urun != null) {
            urunListesi.remove(id);
            kullanilmayanIdler.add(id);
            System.out.println("Urun Listeden silinmistir");
        } else {
            System.out.println("Depoda bu urun bulunmamaktadir");
        }

    }

}
