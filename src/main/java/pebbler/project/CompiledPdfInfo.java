package pebbler.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompiledPdfInfo {
  
  // Only created after compile() in PebblerProject;
  private Map<String, List<String>> cachedFilenames;

  // Map from Variation-PDF name to Comments for that variation.
  private Map<String, String> variationComments;

  public CompiledPdfInfo() {
    cachedFilenames = new HashMap<String, List<String>>();
    variationComments= new HashMap<String, String>();
  }

  // Mape from SGF name to Variation-PDF names.
  public void addFilenames(String key, List<String> filenames) {
    if (cachedFilenames.containsKey(key)) {
      throw new RuntimeException("All generated PDFs must be unique. " + 
        "There was a collision for: " + key);
    }
    cachedFilenames.put(key, filenames);  
  }

  public void addComments(String key, String comments) {
    variationComments.put(key, comments);
  }

  public String getVariationComments(String key) {
    return variationComments.get(key);
  }

  public List<String> getFilenames(String key) {
    return cachedFilenames.get(key);
  }

  public Map<String, List<String>> getAllFilenames() {
    return cachedFilenames;
  }

  public Map<String, String> getAllVariationComments() {
    return variationComments;
  }
}
