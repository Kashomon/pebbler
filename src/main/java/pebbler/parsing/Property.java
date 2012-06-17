package pebbler.goast;

class GoProperty {
  String data = null;
  String token;

  public GoProperty(String token) {
    this.token = token;
  }

  public GoProperty(String token, String data) {
    this.token = token;
    this.data  = data;
  }
}

