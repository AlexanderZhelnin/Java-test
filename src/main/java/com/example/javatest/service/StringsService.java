package com.example.javatest.service;

import org.springframework.stereotype.Service;

/**
 * Сервис для натуральной сортировки строк (аналог Strings.cs)
 */
@Service
public class StringsService {

    /**
     * Натуральное сравнение строк
     */
    public int compare(String s1, String s2) {
        boolean ne1 = (s1 == null || s1.isEmpty());
        boolean ne2 = (s2 == null || s2.isEmpty());

        if (ne1 && ne2) return 0;
        if (ne1) return -1;
        if (ne2) return 1;

        int i1 = 0;
        int i2 = 0;

        while (i1 < s1.length() && i2 < s2.length()) {
            char c1 = s1.charAt(i1);
            char c2 = s2.charAt(i2);

            if (c1 >= '0' && c1 <= '9' && c2 >= '0' && c2 <= '9') {
                int num1 = c1 - '0';
                int num2 = c2 - '0';

                i1++;
                i2++;

                // Читаем остальные цифры первого числа
                while (i1 < s1.length() && s1.charAt(i1) >= '0' && s1.charAt(i1) <= '9') {
                    num1 = num1 * 10 + (s1.charAt(i1) - '0');
                    i1++;
                }

                // Читаем остальные цифры второго числа
                while (i2 < s2.length() && s2.charAt(i2) >= '0' && s2.charAt(i2) <= '9') {
                    num2 = num2 * 10 + (s2.charAt(i2) - '0');
                    i2++;
                }

                if (num1 != num2) return num1 > num2 ? 1 : -1;
            } else {
                // Сравниваем как символы
                if (c1 != c2) return Character.compare(c1, c2);

                i1++;
                i2++;
            }
        }

        return Integer.compare(s2.length(), s1.length());
    }
}
