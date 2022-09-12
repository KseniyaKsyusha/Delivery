package ua.zp.kosse.oksana.tommy.model.part;

    sealed public abstract class Part permits Cube, Sphere, Tetrahedron {

        private double density; // густина, в грамах на 1 мм³
        private int layerThickness;

        public Part(double density) {
            this.layerThickness=30;
            this.density = density;
        }

        public double getDensity() {
            return density;
        }

        public void setDensity(double density) {
            this.density = density;
        }

        public double getProtectiveLayerThickness(double weightInGrams) {

            double weightInKilograms = weightInGrams / 1000;
            return weightInKilograms / this.layerThickness; // Підказка: це є магічною цифрою
        }
    }

