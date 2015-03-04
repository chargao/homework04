package homework04.homework04;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Charlie on 3/3/2015.
 */
/*A subclass containing only a LatLng, String zip, and String county*/
public class LatLongZipCounty {
  LatLng ltlg;
  String zip;
  String county;

  public LatLongZipCounty(double lt, double lg, String zp, String cy) {
    ltlg = new LatLng(lt, lg);
    zip = zp;
    county = cy;
  }

  public LatLng getLtlg() {
    return ltlg;
  }

  public String getZip() {
    return zip;
  }

  public String getCounty() {
    return county;
  }
}