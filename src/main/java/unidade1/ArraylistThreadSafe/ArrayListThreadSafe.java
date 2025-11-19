package unidade1.ArraylistThreadSafe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListThreadSafe<E> {
    private final List<E> LISTA_INTERNA = new ArrayList<>();
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final Lock LEITURA_LOCK = LOCK.readLock();
    private final Lock ESCRITA_LOCK = LOCK.writeLock();

    public void adicionar(E elemento) {
        ESCRITA_LOCK.lock();
        try {
            LISTA_INTERNA.add(elemento);
        } finally {
            ESCRITA_LOCK.unlock();
        }
    }

    public void remover(int index) {
        ESCRITA_LOCK.lock();
        try {
            LISTA_INTERNA.remove(index);
        } finally {
            ESCRITA_LOCK.unlock();
        }
    }


    public E pegar(int index) {
        LEITURA_LOCK.lock();
        try {
            return LISTA_INTERNA.get(index);
        } finally {
            LEITURA_LOCK.unlock();
        }
    }

    public int tamanho() {
        LEITURA_LOCK.lock();
        try {
            return LISTA_INTERNA.size();
        } finally {
            LEITURA_LOCK.unlock();
        }
    }
}
