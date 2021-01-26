package GameLogic.Ships;

public class ShipsParameters {
   private int count;
   private int size;
   
   public ShipsParameters(int count, int size) {
       this.count = count;
       this.size = size;
   }
   
   public int getCount() {
       return this.count;
   }
   
   public int getSize() {
       return this.size;
   }

}
