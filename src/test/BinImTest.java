package test;

import com.classes.Graph.Network.GraphNetwork;
import com.classes.Util;
import org.junit.jupiter.api.Test;

public class BinImTest {

    @Test
    void UnExemple() {
        Util.ResolveBinIm("JeuDeDonneesDemo1prim", "matrix", false);
        Util.ResolveBinIm("JeuDeDonneesDemo1prim", "list", false);
    }
}
