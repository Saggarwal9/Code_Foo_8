
public class Data {
    String guid="";
    String category="";
    String title="";
    String description="";
    String date="";
    String link="";
    String ign_slug="";
    String ign_network="";
    String ign_state="";
    String ign_tags="";
    String ign_thumbnail_compact="";
    String ign_thumbnail_medium="";
    String ign_thumbnail_large="";

    private String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","'",","};
        String outputString="";
        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                outputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
                inputString = outputString;
            }
        }
        return inputString;
    }

    public String getGuid() {
        return escapeMetaCharacters(guid);
    }
    public void setGuid(String guid) {
        this.guid = guid;
    }
    public String getCategory() {
        return escapeMetaCharacters(category);
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getTitle() {
        return escapeMetaCharacters(title);
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return escapeMetaCharacters(description);
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDate() {
        return escapeMetaCharacters(date);
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getLink() {
        return escapeMetaCharacters(link);
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getIgn_slug() {
        return escapeMetaCharacters(ign_slug);
    }
    public void setIgn_slug(String ign_slug) {
        this.ign_slug = ign_slug;
    }
    public String getIgn_network() {
        return escapeMetaCharacters(ign_network);

    }
    public void setIgn_network(String ign_network) {
        this.ign_network = ign_network;
    }
    public String getIgn_state() {
        return escapeMetaCharacters(ign_state);
    }
    public void setIgn_state(String ign_state) {
        this.ign_state = ign_state;
    }
    public String getIgn_tags() {
        return escapeMetaCharacters(ign_tags);
    }
    public void setIgn_tags(String ign_tags) {
        this.ign_tags = ign_tags;
    }
    public String getIgn_thumbnail_compact() {
        return escapeMetaCharacters(ign_thumbnail_compact);
    }
    public void setIgn_thumbnail_compact(String ign_thumbnail_compact) {
        this.ign_thumbnail_compact = ign_thumbnail_compact;
    }
    public String getIgn_thumbnail_medium() {
        return escapeMetaCharacters(ign_thumbnail_medium);
    }
    public void setIgn_thumbnail_medium(String ign_thumbnail_medium) {
        this.ign_thumbnail_medium = ign_thumbnail_medium;
    }
    public String getIgn_thumbnail_large() {
        return escapeMetaCharacters(ign_thumbnail_large);
    }
    public void setIgn_thumbnail_large(String ign_thumbnail_large) {
        this.ign_thumbnail_large = ign_thumbnail_large;
    }


}
