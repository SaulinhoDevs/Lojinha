package com.lojinhasystem.system.entities.enums;

public enum StatusVenda {

    PAGO(1),
    AGUARDANDO_PAGAMENTO(2),
    PARCELADO(3);

    private int codigo;

    StatusVenda(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusVenda valueOf(int codigo) {
        for (StatusVenda value : StatusVenda.values()) {
            if (value.getCodigo() == codigo) {
                return value;
            }
        }
        throw new IllegalArgumentException("Código de Status Inválido!");
    }

}