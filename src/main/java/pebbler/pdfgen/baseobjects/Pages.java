package pebbler.pdfgen.baseobjects;

import java.util.LinkedList;
import java.util.List;

/** Pages contains information linking to other pages.  Note that there is only
 * one Pages superclass
 *
 * Note that for the linked objects will be the 'kids'
 */
class Pages extends BodyObject {
  /* Kid contains information linking to Page objects (below) */
  Pages parent;
  List<Page> pageList = new LinkedList<Page>();
  int count;
  
  public Pages() {
    super();
    Page p = new Page(this);
    linkedObjects.add(p);
    pageList.add(p);
    
    type = "Pages";
    name = "Pages";
  } 
  
  StringBuilder display(StringBuilder currentCode, CrossRef table) {
    currentCode
      .append(pdfHeader())
      .append(pdfType())
      .append("/Count ").append(linkedObjects.size()).append("\n")
      .append("/Kids [");
    for (int i = 0; i < linkedObjects.size(); i++) {
      currentCode.append(linkedObjects.get(i).getReference());
      if (!(i == linkedObjects.size() - 1)) { currentCode.append(" "); }
      else { currentCode.append("]\n"); }
    }
    return currentCode.append(pdfFooter());
  } 
  
  /** Get a free page so we can add something to it (probably a graphic).
   * Note that later, we might want to make this method more fancy so that we
   * can select the optimal page from Pages. */
  protected Page getPage() {
    return pageList.get(0);
  } 
} 

