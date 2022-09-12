package ua.zp.kosse.oksana.tommy.model.part;

import ua.zp.kosse.oksana.tommy.model.packaging.Box;

    public class CalculateBox {

        public static int calculateBoxPrice(Box box, int pricePerMeter) {
            int boxSize = box.getSide();
            int boxArea = 6 * boxSize * boxSize ; // площа поверхні куба в мм²
            double pricePerMilimeter = pricePerMeter / 1000d / 1000d;
            return (int) Math.ceil(boxArea * pricePerMilimeter);
        } // Підказка: чи має цей класс займатися вирахуванням вартості ящику?
}
