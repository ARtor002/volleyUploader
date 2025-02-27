package ir.ARtor.volley.models;

public class Image_Model {
    private String id;
    private String email;
    private String image;
    private String uploaded;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getUploaded_At() {
        return uploaded;
    }
}
