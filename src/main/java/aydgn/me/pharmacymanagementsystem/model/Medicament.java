package aydgn.me.pharmacymanagementsystem.model;

public class Medicament {
    private final Integer medicamentID;
    private final String medicamentName;
    private final String company;
    private final Double pricePerUnit;
    private final Integer quantity;
    private final String description;
    private final String expiryDate;

    public Medicament(Integer medicamentID, String medicamentName, String company, Double pricePerUnit, Integer quantity, String description, String expiryDate) {
        this.medicamentID = medicamentID;
        this.medicamentName = medicamentName;
        this.company = company;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.description = description;
        this.expiryDate = expiryDate;
    }

    public Integer getMedicamentID() {
        return medicamentID;
    }

    public String getMedicamentName() {
        return medicamentName;
    }

    public String getCompany() {
        return company;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
