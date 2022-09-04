package final_project;

public class EditDistance {
	public int editDistance(String wordOne, String wordTwo) {
		int lengthOfWordOne = wordOne.length();
		int lengthOfWordTwo = wordTwo.length();
		int[][] adjacencyMatrix = new int[lengthOfWordOne + 1][lengthOfWordTwo + 1];

		for (int i = 0; i <= lengthOfWordOne; i++)
			adjacencyMatrix[i][0] = i;

		for (int j = 0; j <= lengthOfWordTwo; j++)
			adjacencyMatrix[0][j] = j;

		for (int i = 0; i < lengthOfWordOne; i++)
			for (int j = 0; j < lengthOfWordTwo; j++)
				if (wordOne.charAt(i) == wordTwo.charAt(j))
					adjacencyMatrix[i + 1][j + 1] = adjacencyMatrix[i][j];
				else {
					int replaceLetter = adjacencyMatrix[i][j] + 1;
					int insertLetter = adjacencyMatrix[i][j + 1] + 1;
					int deleteLetter = adjacencyMatrix[i + 1][j] + 1;

					adjacencyMatrix[i + 1][j + 1] = Math.min(Math.min(replaceLetter, insertLetter), deleteLetter);
				}
		return adjacencyMatrix[lengthOfWordOne][lengthOfWordTwo];
	}
}