public class RegionCreator implements Runnable
{
    private int regionCode;

    RegionCreator(int regionCode)
    {
      this.regionCode = regionCode;// стартовый номер для генерации региона
    }

    @Override
    public void run()
    {
        char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};// не моё......
        System.out.println("Генерация региона - " + regionCode + " Процесс пошел ...");

        StringBuilder carNumber = new StringBuilder();
            for (char firstLetter : letters) {
                for (char secondLetter : letters) {
                    for (char thirdLetter : letters) {
                        for (int number = 1; number < 1000; number++) {
                            carNumber.append(firstLetter)
                                    .append(padNumber(number, 3))
                                    .append(secondLetter).append(thirdLetter)
                                    .append(padNumber(regionCode, 2))
                                    .append("\n");
                        }
                    }
                }
            }
        Loader.queue.add(carNumber.toString());// кидаем в кучу номера одного региона

        System.out.println("Генерация региона - " + regionCode + " Закончена !");
    }
    private static String padNumber(int number, int numberLength)
    {
        StringBuilder numberStr = new StringBuilder(Integer.toString(number));
        int padSize = numberLength - numberStr.length();
        for(int i = 0; i < padSize; i++) {
            numberStr.insert(0,'0');
        }
        return numberStr.toString();
    }
}
