package net.staticstudios.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam (GitHub: <a href="https://github.com/Sammster10">Sam's GitHub</a>)
 * <br>
 * <br> This class will hold a list of weighted elements, and can be used to randomly select an element from the list based on their respective weights.
 */
public class WeightedElements<T> {

    /**
     * A static method to get a random element from a list of weighted elements.
     *
     * @param weightedElements A list of weighted elements.
     * @return A random element from the list while factoring in the elements' weights.
     */
    @NotNull
    public static <T> T getRandom(List<WeightedElement<T>> weightedElements) {
        List<T> elements = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        for (WeightedElement<T> weightedElement : weightedElements) elements.add(weightedElement.element);
        for (WeightedElement<T> weightedElement : weightedElements) weights.add(weightedElement.weight);
        double totalWeight = 0;
        for (double weight : weights) totalWeight += weight;
        double random = Math.random() * totalWeight;
        double currentWeight = 0;
        for (int i = 0; i < elements.size(); i++) {
            currentWeight += weights.get(i);
            if (random < currentWeight) return elements.get(i);
        }
        return elements.get(0);
    }

    /**
     * Create a new WeightedElements object.
     *
     * @param o The object to hold.
     * @param weight The weight of the object.
     * @return A new WeightedElements object.
     */
    public static WeightedElement of(Object o, double weight) {
        return new WeightedElement(o, weight);
    }

    //List of weighted elements
    private final List<WeightedElement<T>> elements = new ArrayList<WeightedElement<T>>();

    /**
     * @return The list of weighted elements.
     */
    public List<WeightedElement<T>> getElements() {
        return elements;
    }

    /**
     * @param index Where to get the element from in the list
     * @return The element at the given index.
     *
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public WeightedElement<T> getElement(int index) {
        return elements.get(index);
    }
    /**
     * @param index The position of the object in the list.
     * @return The object at the given index.
     *
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public double getObject(int index) {
        return (elements.get(index).getWeight() / getTotalWeight()) * 100;
    }


    /**
     * @param index The position of the object in the list.
     * @return The weight of the object at the given index.
     *
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public double getChance(int index) {
        return (elements.get(index).getWeight() / getTotalWeight()) * 100;
    }
    /**
     * @param o The object to look for in the list.
     * @return The weight of the object if it is contained in the list, otherwise it will return 0.
     */
    public double getChance(Object o) {
        for (WeightedElement<T> element : elements)
            if (element.element.equals(o)) return (element.getWeight() / getTotalWeight()) * 100;
        return 0;
    }

    /**
     * @return The total weight of all the elements in the list.
     */
    public double getTotalWeight() {
        double totalWeight = 0;
        for (WeightedElement<T> element : elements) totalWeight += element.weight;
        return totalWeight;
    }

    /**
     * @return A new list of objects, with the same elements as the original list, but without having a weight associated with them.
     */
    public List<T> getObjectList() {
        List<T> elements = new ArrayList<>();
        for (WeightedElement<T> weightedElement : this.elements) elements.add(weightedElement.element);
        return elements;
    }

    /**
     * Add an element to the list.
     *
     * @return The current WeightedElements object.
     */
    public WeightedElements<T> add(WeightedElement<T> e) {
        elements.add(e);
        return this;
    }

    /**
     * Creates and adds a new WeightedElement to the internal list.
     *
     * @return The current WeightedElements object.
     */
    public WeightedElements<T> add(T o, double weight) {
        return add(of(o, weight));
    }

    /**
     * @param e The object to remove from the internal list.
     * @return The current WeightedElements object.
     */
    public WeightedElements<T> remove(WeightedElement e) {
        elements.remove(e);
        return this;
    }
    /**
     * Creates a new WeightedElement and remove any element from the internal list that matches the newly created object.
     *
     * @return The current WeightedElements object.
     */
    public WeightedElements<T> remove(T o, double weight) {
        return remove(of(o, weight));
    }

    /**
     * @param e The new list of WeightedElements.
     * @return The current WeightedElements object.
     */
    public WeightedElements<T> set(WeightedElement<T>... e) {
        elements.clear();
        elements.addAll(List.of(e));
        return this;
    }

    /**
     * Clear the internal list.
     *
     * @return The current WeightedElements object.
     */
    public WeightedElements<T> clear() {
        elements.clear();
        return this;
    }

    /**
     * @return A random element from the internal list of all WeightedElements while factoring in each element's weight.
     */
    public T getRandom() {
        return getRandom(elements);
    }



//    public static String example() {
//        //Creates a new WeightedElements object, adds 4 weighted elements to it, and gets a random element from it.
//        return new WeightedElements<String>()
//                .add("There is a 50% chance to return this string.", 50)
//                .add("There is a 25% chance to return this string", 25)
//                .add("There is a 15% chance to return this string", 15)
//                .add("There is a 10% chance to return this string", 10)
//                .getRandom();
//    }
}
