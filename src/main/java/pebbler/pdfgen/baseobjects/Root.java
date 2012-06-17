package pebbler.pdfgen.baseobjects;

/** Root is the first indirect body object that one must have. 
 * Links to: Pages.
 */
class Root extends BodyObject {

  public Root() {
    super();
    linkedObjects.add(new Pages());
    type = "Catalog";
    name = "Root";
  } 
  
  StringBuilder display(StringBuilder currentCode, CrossRef table) {
    return currentCode
      .append(pdfHeader())
      .append(pdfType())
      .append(linkedObjects.get(0).pdfNameRef())
      .append(pdfFooter());
  } 
  
  protected Page getPage() {
    return linkedObjects.get(0).getPage();
  } 
}
