package ir.berimbasket.app.data.network.model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("description")
    private String description;
    @SerializedName("link")
    private String link;
    @SerializedName("slug")
    private String slug;
    @SerializedName("avatar_urls")
    private Avatar avatarUrl;
    @SerializedName("uinstagramid")
    private String instagram;
    @SerializedName("utelegramlid")
    private String telegram;
    @SerializedName("height")
    private String height;
    @SerializedName("weight")
    private String weight;
    @SerializedName("city")
    private String city;
    @SerializedName("age")
    private String age;
    @SerializedName("sex")
    private String sex;
    @SerializedName("coach")
    private String coach;
    @SerializedName("teamname")
    private String teamName;
    @SerializedName("experience")
    private String experience;
    @SerializedName("post")
    private String post;
    @SerializedName("telegramphone")
    private String telegramPhone;

    public Profile(int id, String name, String url, String description, String link, String slug, Avatar avatarUrl, String instagram, String telegram, String height, String weight, String city, String age, String sex, String coach, String teamName, String experience, String post, String telegramPhone) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.description = description;
        this.link = link;
        this.slug = slug;
        this.avatarUrl = avatarUrl;
        this.instagram = instagram;
        this.telegram = telegram;
        this.height = height;
        this.weight = weight;
        this.city = city;
        this.age = age;
        this.sex = sex;
        this.coach = coach;
        this.teamName = teamName;
        this.experience = experience;
        this.post = post;
        this.telegramPhone = telegramPhone;
    }

    public class Avatar {
        @SerializedName("24")
        private String _24dips;
        @SerializedName("48")
        private String _48dips;
        @SerializedName("96")
        private String _96dips;

        public String get_24dips() {
            return _24dips;
        }

        public void set_24dips(String _24dips) {
            this._24dips = _24dips;
        }

        public String get_48dips() {
            return _48dips;
        }

        public void set_48dips(String _48dips) {
            this._48dips = _48dips;
        }

        public String get_96dips() {
            return _96dips;
        }

        public void set_96dips(String _96dips) {
            this._96dips = _96dips;
        }

        public Avatar(String dips, String dips1, String dips2) {
            _24dips = dips;
            _48dips = dips1;
            _96dips = dips2;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Avatar getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(Avatar avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTelegramPhone() {
        return telegramPhone;
    }

    public void setTelegramPhone(String telegramPhone) {
        this.telegramPhone = telegramPhone;
    }
}
