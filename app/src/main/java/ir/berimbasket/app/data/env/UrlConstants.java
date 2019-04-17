package ir.berimbasket.app.data.env;

@SuppressWarnings("ALL")
public interface UrlConstants {

    interface Base {
        String Root = "https://berimbasket.ir";
        String BBAL = "http://berimbasket.ir/bball/";
        String OTP2_PLUGIN = "http://berimbasket.ir/wp-content/plugins/berimbasket-otp2/";
        String MOCKOON = "http://10.0.2.2:3000/";
        String APP = "https://berimbasket.ir/app/";
        String AUTH = "https://berimbasket.ir/wp-json/jwt-auth/v1/";
        String JWT = "https://berimbasket.ir/wp-json/wp/v2/";
        String TELEGRAM = "https://t.me/";
        String INSTAGRAM = "https://instagram.com/_u/";
        String COUNTRY_LIST = "https://restcountries.eu/rest/v2/";
    }

    interface Bot {
        String SCORE = "https://t.me/berimbasketScorebot";
        String PROFILE = "https://t.me/berimbasketProfilebot";
        String REPORT_PLAYER = "https://t.me/berimbasketreportbot?start=";
        String REPORT_STADIUM = "https://t.me/berimbasketreportbot?start=-";
        String REGISTER = "https://t.me/berimbasketbot";
        String MAP = "https://t.me/berimbasketmapbot?start=";
        String RESERVE = "https://t.me/Berimbasketreservebot?start=";
        String UPLOAD = "https://t.me/berimbasketuploadbot?start=";
    }

    interface External {
        String WP_REGISTER = "http://berimbasket.ir/bball/www/register.php";
        String WP_PLAYER_PROFILE = "http://berimbasket.ir/bball/www/player.php";
        String WP_STADIUM_PROFILE = "http://berimbasket.ir/bball/www/instagram.php";
        String STADIUM_DEFAULT_PHOTO = "http://berimbasket.ir/bball/bots/playgroundphoto/123423743522345.jpg";
        String REGISTER_TUTORIAL = "https://t.me/berimbasket/263";
        String HELP = "http://berimbasket.ir/help";
        String TERMS = "http://berimbasket.ir/terms";
        String ABOUT = "http://berimbasket.ir/about";
        String CHANGE_LOG = "http://berimbasket.ir/changelog";
        String CONTACT_US = "http://berimbasket.ir/contact-us";
        String WP_FORGET_PASSWORD = "http://berimbasket.ir/forget-password";
    }
}
