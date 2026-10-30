import java.util.ArrayList;
import java.util.List;

// ==========================================
// 1. PATRÓN PROTOTYPE (Clonación)
// ==========================================
class Ingrediente {
    String nombre;
    double porcentaje;

    public Ingrediente(String nombre, double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }
}

class Formula implements Cloneable {
    String codigo;
    List<Ingrediente> ingredientes;

    public Formula(String codigo, List<Ingrediente> ingredientes) {
        this.codigo = codigo;
        this.ingredientes = ingredientes;
    }

    @Override
    public Formula clone() {
        try {
            Formula copia = (Formula) super.clone();
            copia.ingredientes = new ArrayList<>();
            for (Ingrediente ing : this.ingredientes) {
                copia.ingredientes.add(new Ingrediente(ing.nombre, ing.porcentaje));
            }
            return copia;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar", e);
        }
    }
}

// ==========================================
// 2. PATRÓN BUILDER (Construcción paso a paso)
// ==========================================
class OrdenDeProduccion {
    private final String numero;
    private final double toneladas;

    private OrdenDeProduccion(Builder b) {
        this.numero = b.numero;
        this.toneladas = b.toneladas;
    }

    public static class Builder {
        private String numero;
        private double toneladas;

        public Builder numero(String n) {
            this.numero = n;
            return this;
        }

        public Builder toneladas(double t) {
            this.toneladas = t;
            return this;
        }

        public OrdenDeProduccion build() {
            if (numero == null) {
                throw new IllegalArgumentException("Falta el numero de orden");
            }
            if (toneladas < 5 || toneladas > 200) {
                throw new IllegalArgumentException("Toneladas fuera de rango (5 a 200)");
            }
            return new OrdenDeProduccion(this);
        }
    }
}

// ==========================================
// 3. PATRÓN ABSTRACT FACTORY (Familias)
// ==========================================
interface PerfilNutricional {
    void mostrar();
}

interface FabricaLinea {
    PerfilNutricional crearPerfil();
}

class FabricaTrucha implements FabricaLinea {
    public PerfilNutricional crearPerfil() {
        return new PerfilTrucha();
    }
}

class PerfilTrucha implements PerfilNutricional {
    public void mostrar() {
        System.out.println("Proteina 42%, Lipidos 18%");
    }
}

class FabricaTilapia implements FabricaLinea {
    public PerfilNutricional crearPerfil() {
        return new PerfilTilapia();
    }
}

class PerfilTilapia implements PerfilNutricional {
    public void mostrar() {
        System.out.println("Proteina 28%, Lipidos 6%");
    }
}

// ==========================================
// PROGRAMA PRINCIPAL (Main)
// ==========================================
public class Main {
    public static void main(String[] args) {
        
        // --- PRUEBA PROTOTYPE ---
        List<Ingrediente> lista = new ArrayList<>();
        lista.add(new Ingrediente("Harina de pescado", 38.0));
        
        Formula maestra = new Formula("TRUCHA-M01", lista);
        Formula variante = maestra.clone();
        
        variante.codigo = "V-01";
        variante.ingredientes.get(0).porcentaje = 10.0;
        
        System.out.println("Maestra: " + maestra.ingredientes.get(0).nombre + " " + maestra.ingredientes.get(0).porcentaje + "%");
        System.out.println("Variante: " + variante.ingredientes.get(0).nombre + " " + variante.ingredientes.get(0).porcentaje + "%");

        // --- PRUEBA BUILDER ---
        try {
            OrdenDeProduccion orden = new OrdenDeProduccion.Builder()
                    .numero("OP-2026")
                    .toneladas(60)
                    .build();
            System.out.println("Orden creada con exito");
            
            new OrdenDeProduccion.Builder().numero("OP-ERR").toneladas(250).build();
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR CONTROLADO] " + e.getMessage());
        }

        // --- PRUEBA ABSTRACT FACTORY ---
        FabricaLinea fabrica = new FabricaTrucha();
        PerfilNutricional perfil = fabrica.crearPerfil();
        perfil.mostrar();
    }
}