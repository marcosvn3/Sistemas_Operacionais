package unidade1.Barbeiro;

class Cliente {
    private final int id;

    public Cliente(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Cliente #" + id;
    }
}