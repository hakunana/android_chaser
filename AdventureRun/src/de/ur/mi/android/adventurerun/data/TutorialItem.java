package de.ur.mi.android.adventurerun.data;

public class TutorialItem {

  private int drawableRessourceId;
  private String explanation;
  private String headline;

  public TutorialItem(int drawableRessourceId, String explanation, String headline) {
    this.drawableRessourceId = drawableRessourceId;
    this.explanation = explanation;
    this.headline = headline;
  }

  public String getHeadline() {
    return headline;
  }

  public String getExplanation() {
    return explanation;
  }

  public int getDrawableRessourceId() {
    return drawableRessourceId;
  }
}