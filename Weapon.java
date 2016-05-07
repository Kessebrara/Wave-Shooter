public class Weapon
{
    private double attackSpeedBase; // Higher = better
    private double magazineSizeBase;
    private double projectileSpeedBase;
    private double projectileRangeBase;
    private double reloadSpeedBase;
    
    
    public Weapon()
    {
    }
    
    public Weapon(double attackSpeed, double magazineSize, double projectileRange,
        double projectileSpeed, double reloadSpeed)
    {
        attackSpeedBase = attackSpeed;
        magazineSizeBase = magazineSize;
        projectileRangeBase = projectileRange;
        projectileSpeedBase = projectileSpeed;
        reloadSpeedBase = reloadSpeed;
    }
    
    public Weapon(Weapon w)
    {
        this(w.getAttackSpeed(), w.getMagazineSize(), w.getProjectileRange(),
            w.getProjectileSpeed(), w.getReloadSpeed());
    }
    
    public double getAttackSpeed()      { return attackSpeedBase; }    
    public double getMagazineSize()     { return magazineSizeBase; } 
    public double getProjectileRange()  { return projectileRangeBase; }    
    public double getProjectileSpeed()  { return projectileSpeedBase; }
    public double getReloadSpeed()      { return reloadSpeedBase; }
    
}