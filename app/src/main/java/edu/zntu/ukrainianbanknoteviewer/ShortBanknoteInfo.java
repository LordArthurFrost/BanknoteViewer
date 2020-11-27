package edu.zntu.ukrainianbanknoteviewer;

public class ShortBanknoteInfo
{

    private String denomination;
    private String printYear;
    private String releaseDate;
    private String memorable;
    private String active;
    private String imageAbver;
    private String imageRever;

    public ShortBanknoteInfo(String id, String denomination, String printYear, String releaseDate, String memorable, String active)
    {
        this.denomination = denomination;
        this.printYear = printYear;
        this.releaseDate = releaseDate;
        this.memorable = memorable;
        this.active = active;
        this.imageAbver = id;
        this.imageRever = id + 1;
    }

    public String getDenomination()
    {
        return denomination;
    }

    public String getPrintYear()
    {
        return printYear;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public String getMemorable()
    {
        return memorable;
    }

    public String getActive()
    {
        return active;
    }

    public String getImageAbver()
    {
        return imageAbver;
    }

}
