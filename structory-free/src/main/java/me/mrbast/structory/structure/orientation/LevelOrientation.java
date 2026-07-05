package me.mrbast.structory.structure.orientation;

import org.bukkit.Location;

import java.util.function.Function;

public enum LevelOrientation {

    NORTH {
        @Override
        public Orientation from(Orientation orientation) {
            switch (orientation.getOrientation()) {
                case EAST:
                    return new Orientation(-orientation.getZ().doubleValue(), orientation.getX().doubleValue(), NORTH);
                case SOUTH:
                    return new Orientation(-orientation.getX().doubleValue(), -orientation.getZ().doubleValue(), NORTH);
                case WEST:
                    return new Orientation(orientation.getZ(), -orientation.getX().doubleValue(), NORTH);
                default:
                    return new Orientation(orientation.getX(), orientation.getZ(), NORTH);
            }
        }

        @Override
        public Orientation from(Orientation orientation, Location center) {
            double relativeX = orientation.getX().doubleValue() - center.getBlockX();
            double relativeZ = orientation.getZ().doubleValue() - center.getBlockZ();
            Orientation relativeOrientation = new Orientation(relativeX, relativeZ, orientation.getOrientation());
            Orientation transformedOffset = this.from(relativeOrientation);
            return new Orientation(center.getBlockX() + transformedOffset.getX().doubleValue(), center.getBlockZ() + transformedOffset.getZ().doubleValue(), this);
        }
    },
    SOUTH {
        @Override
        public Orientation from(Orientation orientation) {
            switch (orientation.getOrientation()) {
                case EAST:
                    return new Orientation(orientation.getZ().doubleValue(), -orientation.getX().doubleValue(), SOUTH);
                case NORTH:
                    return new Orientation(-orientation.getX().doubleValue(), -orientation.getZ().doubleValue(), SOUTH);
                case WEST:
                    return new Orientation(-orientation.getZ().doubleValue(), orientation.getX().doubleValue(), SOUTH);
                default:
                    return new Orientation(orientation.getX().doubleValue(), orientation.getZ().doubleValue(), SOUTH);
            }
        }

        @Override
        public Orientation from(Orientation orientation, Location center) {
            double relativeX = orientation.getX().doubleValue() - center.getBlockX();
            double relativeZ = orientation.getZ().doubleValue() - center.getBlockZ();
            Orientation relativeOrientation = new Orientation(relativeX, relativeZ, orientation.getOrientation());
            Orientation transformedOffset = this.from(relativeOrientation);
            return new Orientation(center.getBlockX() + transformedOffset.getX().doubleValue(), center.getBlockZ() + transformedOffset.getZ().doubleValue(), this);
        }
    },
    WEST {
        @Override
        public Orientation from(Orientation orientation) {
            switch (orientation.getOrientation()) {
                case EAST:
                    return new Orientation(-orientation.getX().doubleValue(), -orientation.getZ().doubleValue(), WEST);
                case SOUTH:
                    return new Orientation(orientation.getZ().doubleValue(), -orientation.getX().doubleValue(), WEST);
                case NORTH:
                    return new Orientation(-orientation.getZ().doubleValue(), orientation.getX().doubleValue(), WEST);
                default:
                    return new Orientation(orientation.getX().doubleValue(), orientation.getZ().doubleValue(), WEST);
            }
        }

        @Override
        public Orientation from(Orientation orientation, Location center) {
            double relativeX = orientation.getX().doubleValue() - center.getBlockX();
            double relativeZ = orientation.getZ().doubleValue() - center.getBlockZ();
            Orientation relativeOrientation = new Orientation(relativeX, relativeZ, orientation.getOrientation());
            Orientation transformedOffset = this.from(relativeOrientation);
            return new Orientation(center.getBlockX() + transformedOffset.getX().doubleValue(), center.getBlockZ() + transformedOffset.getZ().doubleValue(), this);
        }
    },
    EAST {
        @Override
        public Orientation from(Orientation orientation) {
            switch (orientation.getOrientation()) {
                case NORTH:
                    return new Orientation(orientation.getZ().doubleValue(), -orientation.getX().doubleValue(), EAST);
                case SOUTH:
                    return new Orientation(-orientation.getZ().doubleValue(), orientation.getX().doubleValue(), EAST);
                case WEST:
                    return new Orientation(-orientation.getX().doubleValue(), -orientation.getZ().doubleValue(), EAST);
                default:
                    return new Orientation(orientation.getX().doubleValue(), orientation.getZ().doubleValue(), EAST);
            }
        }

        @Override
        public Orientation from(Orientation orientation, Location center) {
            double relativeX = orientation.getX().doubleValue() - center.getBlockX();
            double relativeZ = orientation.getZ().doubleValue() - center.getBlockZ();
            Orientation relativeOrientation = new Orientation(relativeX, relativeZ, orientation.getOrientation());
            Orientation transformedOffset = this.from(relativeOrientation);
            return new Orientation(center.getBlockX() + transformedOffset.getX().doubleValue(), center.getBlockZ() + transformedOffset.getZ().doubleValue(), this);
        }
    };

    public abstract Orientation from(Orientation orientation);

    public abstract Orientation from(Orientation orientation, Location center);
}