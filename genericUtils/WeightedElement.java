package net.staticstudios.utils;


/**
 * @author Sam (GitHub: <a href="https://github.com/Sammster10">Sam's GitHub</a>)
 * <br>
 * <br> A simple class that is used to hold an object and a weight, used primarily by the <b>WeightedElements</b> class.
 */
public class WeightedElement<T> {
    T element;
    double weight;
    public WeightedElement(T element, double weight) {
        this.weight = weight;
        this.element = element;
    }

    public T getElement() {
        return element;
    }
    public double getWeight() {
        return weight;
    }
}
