module ru.vsu.cs.uvarov_d_p.cg {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.vsu.cs.uvarov_d_p.cg.rasterizationfxapp to javafx.fxml;
    exports ru.vsu.cs.uvarov_d_p.cg.rasterizationfxapp;
}
