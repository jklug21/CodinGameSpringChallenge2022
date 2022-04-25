package spring2022.util;

import spring2022.GameParameters;
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

    public static int timeToCollision(Hero hero, Entity monster) {
        for (int i = 0; i < 10; i++) {
            if (predictDistance(i, hero, monster) < GameParameters.HERO_SPEED * i + 800) {
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
        /*} else if (vx == 0) {
            if (vy < 0) {
                if (x <= 5000) {
                    if (ownBase.getX() == 0) {
                        this.threatFor = Faction.OWN;
                    } else if (enemyBase.getX() == 0) {
                        this.threatFor = Faction.ENEMY;
                    }
                } else {
                    this.threatFor = Faction.MONSTER;
                }
            } else {
                if (x >= 4000) {
                    if (ownBase.getX() == 0) {
                        this.threatFor = Faction.ENEMY;
                    } else if (enemyBase.getX() == 0) {
                        this.threatFor = Faction.OWN;
                    }
                } else {
                    this.threatFor = Faction.MONSTER;
                }
            }*/
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
                double k = (double) vx / (double) vy;
                double d = y - k * x;

                if (potentialThreat == Faction.ENEMY) {
                    double ye = k * enemyBase.getX() + d;
                    double xe = (enemyBase.getY() - d) / k;
                } else {
                    double ye = k * ownBase.getX() + d;
                    double xe = (ownBase.getY() - d) / k;
                }
            }
        }
        return threatFor;
    }

}
