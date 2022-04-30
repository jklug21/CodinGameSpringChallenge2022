package spring2022.util;

import java.util.Arrays;
import java.util.function.Supplier;
import spring2022.GameParameters;
import spring2022.GameState;
import spring2022.behavior.HeroBehaviorContainer;
import spring2022.behavior.HeroClass;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;

public class Helpers {
    public static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.hypot(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    public static double predictDistance(int rounds, Entity entity, Coordinate target) {
        return entity.predictPosition(rounds).distanceTo(target);
    }

    public static double predictDistance(int rounds, Hero hero, Entity target) {
        return hero.distanceTo(target.predictPosition(rounds));
    }

    public static double predictDistance(int rounds, Hero hero, Coordinate targetPosition, Coordinate targetVelocity) {
        return hero.distanceTo(predictPosition(targetPosition, targetVelocity, rounds));
    }

    public static int timeToCollision(Hero hero, Entity target, int distance) {
        return timeToCollision(hero, target.getPosition(), target.getVelocity(), distance);
    }

    public static int timeToCollision(Hero hero, Coordinate targetPosition, Coordinate targetVelocity, int distance) {
        for (int i = 0; i < 10; i++) {
            if (predictDistance(i, hero, targetPosition, targetVelocity) < GameParameters.HERO_SPEED * i + distance) {
                return i;
            }
        }
        return 10; // bigger values should be irrelevant
    }

    public static Faction calculateThreatFor(int x, int y, int vx, int vy, Coordinate enemyBase, Coordinate ownBase) {
        Faction threatFor = Faction.MONSTER;
        Coordinate position = new Coordinate(x, y);
        if (position.distanceTo(ownBase) < 5000) {
            threatFor = Faction.OWN;
        } else if (position.distanceTo(enemyBase) < 5000) {
            threatFor = Faction.ENEMY;
        } else {
            if (vy == 0) {
                if (vx < 0) {
                    if (y <= 5000) {
                        if (ownBase.getX() == 0) {
                            threatFor = Faction.OWN;
                        } else if (enemyBase.getX() == 0) {
                            threatFor = Faction.ENEMY;
                        }
                    }
                } else if (vx > 0) {
                    if (y >= 4000) {
                        if (ownBase.getX() == 0) {
                            threatFor = Faction.ENEMY;
                        } else if (enemyBase.getX() == 0) {
                            threatFor = Faction.OWN;
                        }
                    }
                }
            } else {
                Faction potentialThreat;
                // which base is it heading
                if (ownBase.getX() == 0) {
                    if (vx < 0) {
                        potentialThreat = Faction.OWN;
                    } else {
                        potentialThreat = Faction.ENEMY;
                    }
                } else {
                    if (vx < 0) {
                        potentialThreat = Faction.ENEMY;
                    } else {
                        potentialThreat = Faction.OWN;
                    }
                }

                // y = kx + d
                // calculate k
                double k = (double) vy / (double) vx;
                double d = y - k * x;

                Coordinate base;
                if (potentialThreat == Faction.ENEMY) {
                    base = enemyBase;
                } else {
                    base = ownBase;
                }

                double ye = k * base.getX() + d;
                double xe = (base.getY() - d) / k;
                if (base.getX() == 0 && xe > 0 && xe < 5000 ||
                        base.getX() != 0 && xe < base.getX() && xe > base.getX() - 5000 ||
                        base.getY() == 0 && ye > 0 && ye < 5000 ||
                        base.getY() != 0 && ye < base.getY() && ye > base.getY() - 5000) {
                    threatFor = potentialThreat;
                }
            }
        }
        return threatFor;
    }

    public static Coordinate predictPosition(Coordinate position, Coordinate velocity, int rounds) {
        int x = position.getX() + velocity.getX() * rounds;
        int y = position.getY() + velocity.getY() * rounds;
        return new Coordinate(x, y);
    }

    public static boolean insideMapArea(Coordinate pos) {
        return !(pos.getX() < 0 || pos.getY() < 0 || pos.getX() > 17630 || pos.getY() > 9000);
    }

    public static void changeClass(Hero hero, HeroClass toClass, Supplier<Boolean> until) {
        int heroId = hero.getId();
        changeClass(heroId, toClass, until);
    }

    public static void changeClass(int heroId, HeroClass toClass, Supplier<Boolean> until) {
        GameState state = GameState.get();
        HeroBehaviorContainer behavior = state.getHeroBehaviors().get(heroId % 3);
        behavior.setTempClass(toClass);
        behavior.setEndCondition(until);
    }

    public static int[][] getPermutations(int[] elements) {
        int n = elements.length;
        int[][] permutations = new int[6][3];
        int[] indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }

        int perm = 0;
        permutations[perm++] = Arrays.copyOf(elements, elements.length);

        int i = 0;
        while (i < n) {
            if (indexes[i] < i) {
                swap(elements, i % 2 == 0 ? 0 : indexes[i], i);
                permutations[perm++] = Arrays.copyOf(elements, elements.length);
                indexes[i]++;
                i = 0;
            } else {
                indexes[i] = 0;
                i++;
            }
        }

        return permutations;
    }

    private static void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }
}
