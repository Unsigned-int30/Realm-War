package Utils;

// برای مشخص کردن تایپ منابع استفاده میشه
public class ResourceYield {
    private final ResourceType type; // (GOLD, FOOD, NONE)
    private final int amount;

    public ResourceYield(ResourceType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ResourceType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    // برای جلوگیری از چاپ هش کد منبع اورراید شده
    @Override
    public String toString() {
        if (type == ResourceType.NONE || amount == 0) {
            return "No resources";
        }
        return type.name() + ": " + amount;
    }
}