package test;

import com.classes.Graph.Network.GraphNetwork;
import com.classes.Util;
import org.junit.jupiter.api.Test;

public class BinImTest {

    @Test
    void UnExemple() {
        Util.ResolveBinIm("JeuDeDonnees", "matrix", false);
        Util.ResolveBinIm("JeuDeDonnees", "list", false);
    }
}
