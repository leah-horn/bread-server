package bread.object;

import org.immutables.value.Value;
import javax.measure.Unit;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;

@Value.Immutable
public interface Ingredient {
    Description getDescription();

    /**
     * Unless specified, the preferred SI mass unit of measurement
     * @return Preferred SI mass unit
     */
    Unit<Mass> getNaturalSIMassUnit();

    /**
     * Unless specified, the preferred imperial mass unit of measurement
     * @return Preferred imperial mass unit
     */
    Unit<Mass> getNaturalImperialMassUnit();

    /**
     * Unless specified, the preferred SI mass volume of measurement
     * @return Preferred SI volume unit
     */
    Unit<Volume> getNaturalSIVolumeUnit();

    /**
     * Unless specified, the preferred imperial volume unit of measurement
     * @return Preferred imperial volume unit
     */
    Unit<Volume> getNaturalImperialVolumeUnit();

}
