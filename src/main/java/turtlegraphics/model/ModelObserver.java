package turtlegraphics.model;

/**
 * Observer interface for receiving notifications when the TurtleModel state changes.
 * 
 * <p>This interface is part of the Observer pattern implementation, allowing the View
 * component to be notified of model changes without creating a direct dependency from
 * the Model to the View.</p>
 * 
 * <p>Classes that implement this interface (typically View components) can register
 * themselves with a TurtleModel to receive notifications whenever the model's state
 * changes. This enables automatic view updates in response to model changes.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * public class TurtleView implements ModelObserver {
 *     private TurtleModel model;
 *     
 *     public TurtleView(TurtleModel model) {
 *         this.model = model;
 *         model.addObserver(this);
 *     }
 *     
 *     {@literal @}Override
 *     public void modelChanged() {
 *         // Refresh the view to reflect the new model state
 *         repaint();
 *     }
 * }
 * </pre>
 * 
 * @see TurtleModel#addObserver(ModelObserver)
 * @see TurtleModel#notifyObservers()
 */
public interface ModelObserver {
    
    /**
     * Called when the observed TurtleModel's state has changed.
     * 
     * <p>This method is invoked by the TurtleModel whenever any of its state changes,
     * including:</p>
     * <ul>
     *   <li>Position changes (via move)</li>
     *   <li>Heading changes (via turn)</li>
     *   <li>Pen state changes (via setPenDown, penUp, penDown)</li>
     *   <li>Line additions (via move with pen down)</li>
     *   <li>Line clearing (via clear)</li>
     *   <li>State reset (via reset)</li>
     * </ul>
     * 
     * <p>Implementations should query the model for its current state and update
     * their display accordingly. The method should execute quickly to avoid blocking
     * the model's operations.</p>
     */
    void modelChanged();
}
